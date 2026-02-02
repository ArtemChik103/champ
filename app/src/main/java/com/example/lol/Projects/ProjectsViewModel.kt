package com.example.lol.Projects

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.TokenManager
import com.example.lol.data.network.models.ProjectApi
import com.example.lol.data.repository.IProjectRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Состояние загрузки проектов. */
sealed class ProjectsState {
    object Idle : ProjectsState()
    object Loading : ProjectsState()
    object Success : ProjectsState()
    data class Error(val message: String) : ProjectsState()
}

/**
 * ViewModel для управления проектами. Использует IProjectRepository для сетевых запросов с fallback
 * на SharedPreferences.
 */
class ProjectsViewModel(
        application: Application,
        private val projectRepository: IProjectRepository? = null,
        private val tokenManager: TokenManager? = null
) : AndroidViewModel(application) {

    private val sharedPrefs =
            application.getSharedPreferences("projects_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _projectsState = MutableStateFlow<ProjectsState>(ProjectsState.Idle)
    val projectsState: StateFlow<ProjectsState> = _projectsState.asStateFlow()

    init {
        loadProjects()
    }

    /** Загрузка проектов: сначала API, затем fallback на локальное хранилище. */
    fun loadProjects() {
        viewModelScope.launch {
            _projectsState.value = ProjectsState.Loading

            val repo = projectRepository
            if (repo != null) {
                when (val result = repo.getProjects()) {
                    is NetworkResult.Success -> {
                        // Конвертируем ProjectApi в локальные Project
                        val apiProjects = result.data.map { it.toLocalProject() }
                        _projects.value = apiProjects.sortedByDescending { it.createdAt }
                        // Сохраняем в кэш
                        saveProjectsToCache()
                        _projectsState.value = ProjectsState.Success
                    }
                    is NetworkResult.Error -> {
                        // Fallback на локальное хранилище
                        loadProjectsFromCache()
                        _projectsState.value = ProjectsState.Error(result.message)
                    }
                    is NetworkResult.Loading -> {}
                }
            } else {
                // Нет репозитория - используем только локальное хранилище
                loadProjectsFromCache()
                _projectsState.value = ProjectsState.Success
            }
        }
    }

    /** Загрузка проектов из SharedPreferences (кэш/offline). */
    private fun loadProjectsFromCache() {
        val json = sharedPrefs.getString("projects", null)
        if (json != null) {
            val type = object : TypeToken<List<Project>>() {}.type
            val loadedProjects: List<Project> = gson.fromJson(json, type)
            _projects.value = loadedProjects.sortedByDescending { it.createdAt }
        }
    }

    /** Сохранение проектов в SharedPreferences. */
    private fun saveProjectsToCache() {
        val json = gson.toJson(_projects.value)
        sharedPrefs.edit().putString("projects", json).apply()
    }

    /** Добавление нового проекта. Синхронизирует с API если доступен. */
    fun addProject(project: Project, imageFile: File? = null) {
        viewModelScope.launch {
            _projectsState.value = ProjectsState.Loading

            // Добавляем локально сразу
            val updatedList = _projects.value.toMutableList()
            updatedList.add(0, project)
            _projects.value = updatedList
            saveProjectsToCache()

            // Синхронизация с API
            val repo = projectRepository
            val userId = tokenManager?.getUserId()

            if (repo != null && userId != null) {
                when (val result =
                                repo.createProject(
                                        title = project.name,
                                        typeProject = project.type,
                                        userId = userId,
                                        dateStart = project.startDate,
                                        dateEnd = project.endDate,
                                        gender = project.recipient,
                                        descriptionSource = project.descriptionSource,
                                        category = project.category,
                                        image = imageFile
                                )
                ) {
                    is NetworkResult.Success -> {
                        // Обновляем локальный проект с данными от сервера
                        _projects.value =
                                _projects.value.map {
                                    if (it.id == project.id) {
                                        it.copy(id = result.data.id)
                                    } else {
                                        it
                                    }
                                }
                        saveProjectsToCache()
                        _projectsState.value = ProjectsState.Success
                    }
                    is NetworkResult.Error -> {
                        // Проект уже добавлен локально, показываем ошибку
                        _projectsState.value = ProjectsState.Error(result.message)
                    }
                    is NetworkResult.Loading -> {}
                }
            } else {
                _projectsState.value = ProjectsState.Success
            }
        }
    }

    /** Удаление проекта по ID. */
    fun removeProject(projectId: String) {
        _projects.value = _projects.value.filter { it.id != projectId }
        saveProjectsToCache()
    }

    /** Получение проекта по ID. */
    fun getProjectById(projectId: String): Project? {
        return _projects.value.find { it.id == projectId }
    }

    /** Получение относительного времени для отображения. */
    fun getRelativeTime(createdAt: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - createdAt

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 1 -> "Прошло $days дня"
            days == 1L -> "Вчера"
            hours > 1 -> "Прошло $hours часов"
            hours == 1L -> "Прошел 1 час"
            minutes > 1 -> "Прошло $minutes минут"
            else -> "Только что"
        }
    }

    /** Получение уникальных категорий из products.json asset. */
    fun getCategories(): List<String> {
        return try {
            val json =
                    getApplication<Application>()
                            .assets
                            .open("products.json")
                            .bufferedReader()
                            .use { it.readText() }
            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
            val products: List<Map<String, Any>> = gson.fromJson(json, type)
            products.mapNotNull { it["category"] as? String }.distinct()
        } catch (e: Exception) {
            listOf("Популярные", "Новинки", "Мужское", "Женское", "Аксессуары")
        }
    }

    /** Сброс состояния ошибки. */
    fun resetState() {
        _projectsState.value = ProjectsState.Idle
    }

    /** Конвертация ProjectApi в локальный Project. */
    private fun ProjectApi.toLocalProject(): Project {
        return Project(
                id = this.id,
                name = this.title,
                type = "", // typeProject не возвращается в ответе
                startDate = this.dateStart,
                endDate = this.dateEnd,
                recipient = this.gender,
                descriptionSource = this.descriptionSource,
                category = this.category,
                imageUri =
                        if (this.image.isNotEmpty()) {
                            "https://dsn-vypryamitel.ru/api/files/${this.collectionId}/${this.id}/${this.image}"
                        } else null,
                createdAt = parseDate(this.created)
        )
    }

    /** Парсинг даты из строки API. */
    private fun parseDate(dateString: String): Long {
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .parse(dateString)
                    ?.time
                    ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}

/** Factory для создания ProjectsViewModel с зависимостями. */
class ProjectsViewModelFactory(
        private val application: Application,
        private val projectRepository: IProjectRepository? = null,
        private val tokenManager: TokenManager? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ProjectsViewModel(application, projectRepository, tokenManager) as T
    }
}

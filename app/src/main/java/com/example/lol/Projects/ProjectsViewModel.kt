package com.example.lol.Projects

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** ViewModel for managing projects with local storage using SharedPreferences. */
class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs =
            application.getSharedPreferences("projects_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    init {
        loadProjects()
    }

    /** Load projects from SharedPreferences */
    private fun loadProjects() {
        val json = sharedPrefs.getString("projects", null)
        if (json != null) {
            val type = object : TypeToken<List<Project>>() {}.type
            val loadedProjects: List<Project> = gson.fromJson(json, type)
            _projects.value = loadedProjects.sortedByDescending { it.createdAt }
        }
    }

    /** Save projects to SharedPreferences */
    private fun saveProjects() {
        val json = gson.toJson(_projects.value)
        sharedPrefs.edit().putString("projects", json).apply()
    }

    /** Add a new project */
    fun addProject(project: Project) {
        val updatedList = _projects.value.toMutableList()
        updatedList.add(0, project)
        _projects.value = updatedList
        saveProjects()
    }

    /** Remove a project by ID */
    fun removeProject(projectId: String) {
        _projects.value = _projects.value.filter { it.id != projectId }
        saveProjects()
    }

    /** Get project by ID */
    fun getProjectById(projectId: String): Project? {
        return _projects.value.find { it.id == projectId }
    }

    /** Get relative time string for project display */
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

    /** Get unique categories from products.json asset */
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
}

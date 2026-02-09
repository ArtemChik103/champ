package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProjectApi
import com.example.lol.data.repository.IProjectRepository
import java.io.File

/** Fake реализация IProjectRepository для unit-тестов. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class FakeProjectRepository : IProjectRepository {

    private var getProjectsResult: NetworkResult<List<ProjectApi>> =
            NetworkResult.Success(emptyList())
    private var createProjectResult: NetworkResult<ProjectApi> =
            NetworkResult.Error("Not configured")

    var getProjectsCallCount = 0
        private set
    var createProjectCallCount = 0
        private set

    var lastCreatedTitle: String? = null
        private set
    var lastCreatedCategory: String? = null
        private set

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param projects Список проектов для сохранения или отображения.
     */
    fun setGetProjectsSuccess(projects: List<ProjectApi>) {
        getProjectsResult = NetworkResult.Success(projects)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setGetProjectsError(message: String, code: Int? = null) {
        getProjectsResult = NetworkResult.Error(message, code)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param project Данные проекта для добавления, обновления или отображения.
     */
    fun setCreateProjectSuccess(project: ProjectApi) {
        createProjectResult = NetworkResult.Success(project)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setCreateProjectError(message: String, code: Int? = null) {
        createProjectResult = NetworkResult.Error(message, code)
    }

    // Сбрасывает состояние к исходным значениям по умолчанию.
    fun reset() {
        getProjectsCallCount = 0
        createProjectCallCount = 0
        lastCreatedTitle = null
        lastCreatedCategory = null
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getProjects(): NetworkResult<List<ProjectApi>> {
        getProjectsCallCount++
        return getProjectsResult
    }

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param title Заголовок, который отображается в интерфейсе.
     * @param typeProject Тип проекта, отправляемый при создании записи.
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param dateStart Значение времени `dateStart` для вычислений или форматирования.
     * @param dateEnd Значение времени `dateEnd` для вычислений или форматирования.
     * @param gender Выбранный пол пользователя или проекта.
     * @param descriptionSource Описание проекта, которое передается в API.
     * @param category Категория, по которой выполняется фильтрация или сохранение.
     * @param image Файл изображения, прикрепляемый к сетевому запросу.
     */
    override suspend fun createProject(
            title: String,
            typeProject: String,
            userId: String,
            dateStart: String,
            dateEnd: String,
            gender: String,
            descriptionSource: String,
            category: String,
            image: File?
    ): NetworkResult<ProjectApi> {
        createProjectCallCount++
        lastCreatedTitle = title
        lastCreatedCategory = category
        return createProjectResult
    }
}

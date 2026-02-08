package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProjectApi
import com.example.lol.data.repository.IProjectRepository
import java.io.File

/** Fake реализация IProjectRepository для unit-тестов. */
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

    fun setGetProjectsSuccess(projects: List<ProjectApi>) {
        getProjectsResult = NetworkResult.Success(projects)
    }

    fun setGetProjectsError(message: String, code: Int? = null) {
        getProjectsResult = NetworkResult.Error(message, code)
    }

    fun setCreateProjectSuccess(project: ProjectApi) {
        createProjectResult = NetworkResult.Success(project)
    }

    fun setCreateProjectError(message: String, code: Int? = null) {
        createProjectResult = NetworkResult.Error(message, code)
    }

    fun reset() {
        getProjectsCallCount = 0
        createProjectCallCount = 0
        lastCreatedTitle = null
        lastCreatedCategory = null
    }

    override suspend fun getProjects(): NetworkResult<List<ProjectApi>> {
        getProjectsCallCount++
        return getProjectsResult
    }

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

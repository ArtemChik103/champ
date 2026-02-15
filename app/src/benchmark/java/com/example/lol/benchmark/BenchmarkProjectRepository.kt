package com.example.lol.benchmark

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProjectApi
import com.example.lol.data.repository.IProjectRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.delay

class BenchmarkProjectRepository(private val simulatedNetworkDelayMs: Long = 420L) :
        IProjectRepository {

    override suspend fun getProjects(): NetworkResult<List<ProjectApi>> {
        return NetworkResult.Success(emptyList())
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
        delay(simulatedNetworkDelayMs)
        val timestamp =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        return NetworkResult.Success(
                ProjectApi(
                        id = UUID.randomUUID().toString(),
                        collectionId = "benchmark_collection",
                        collectionName = "project",
                        created = timestamp,
                        updated = timestamp,
                        title = title,
                        dateStart = dateStart,
                        dateEnd = dateEnd,
                        gender = gender,
                        descriptionSource = descriptionSource,
                        category = category,
                        image = image?.name.orEmpty(),
                        userId = userId
                )
        )
    }
}

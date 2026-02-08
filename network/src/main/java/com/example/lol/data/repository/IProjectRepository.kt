package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProjectApi
import java.io.File

/** Интерфейс репозитория для операций с проектами. */
interface IProjectRepository {

    /**
     * Получение списка проектов.
     * @return Список проектов
     */
    suspend fun getProjects(): NetworkResult<List<ProjectApi>>

    /**
     * Создание нового проекта.
     * @param title Название проекта
     * @param typeProject Тип проекта
     * @param userId ID пользователя
     * @param dateStart Дата начала
     * @param dateEnd Дата окончания
     * @param gender Пол
     * @param descriptionSource Описание источника
     * @param category Категория
     * @param image Файл изображения (опционально)
     * @return Созданный проект
     */
    suspend fun createProject(
            title: String,
            typeProject: String,
            userId: String,
            dateStart: String,
            dateEnd: String,
            gender: String,
            descriptionSource: String,
            category: String,
            image: File? = null
    ): NetworkResult<ProjectApi>
}

package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProjectApi
import java.io.File

/** Интерфейс репозитория для операций с проектами. */
// Определяет контракт репозитория для операций с доменными данными.
interface IProjectRepository {

    /**
     * Получение списка проектов.
     * @return Список проектов
     */
    // Возвращает актуальные данные из текущего источника состояния.
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

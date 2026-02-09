package com.example.lol.testdata

import com.example.lol.data.network.models.*

/** Тестовые данные и JSON ответы для unit-тестов. */
// Содержит тестовые JSON-ответы для модульных и интеграционных сценариев.
object TestJsonResponses {

    // ==================== AUTH ====================

    // ==================== АВТОРИЗАЦИЯ ====================
    val successLoginJson =
            """
        {
            "record": {
                "id": "user123",
                "collectionId": "users",
                "collectionName": "_pb_users_auth_",
                "created": "2026-01-01T00:00:00Z",
                "updated": "2026-01-01T00:00:00Z",
                "emailVisibility": true,
                "firstname": "Иван",
                "lastname": "Иванов",
                "secondname": "Иванович",
                "verified": true,
                "datebirthday": "1990-01-01",
                "gender": "Мужской"
            },
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test"
        }
    """.trimIndent()

    val successRegisterJson =
            """
        {
            "id": "user456",
            "collectionId": "users",
            "collectionName": "_pb_users_auth_",
            "created": "2026-01-02T00:00:00Z",
            "updated": "2026-01-02T00:00:00Z",
            "emailVisibility": true,
            "firstname": "",
            "lastname": "",
            "secondname": "",
            "verified": false,
            "datebirthday": "",
            "gender": ""
        }
    """.trimIndent()

    val errorJson =
            """
        {
            "status": 400,
            "message": "Failed to create record.",
            "data": {}
        }
    """.trimIndent()

    val invalidCredentialsJson =
            """
        {
            "status": 400,
            "message": "Invalid credentials.",
            "data": {}
        }
    """.trimIndent()

    // ==================== PRODUCTS ====================

    // ==================== ТОВАРЫ ====================
    val productsListJson =
            """
        {
            "page": 1,
            "perPage": 30,
            "totalPages": 1,
            "totalItems": 2,
            "items": [
                {
                    "id": "prod1",
                    "title": "Футболка",
                    "price": 1500,
                    "typeCloses": "Верх",
                    "type": "Новинки"
                },
                {
                    "id": "prod2",
                    "title": "Джинсы",
                    "price": 3000,
                    "typeCloses": "Низ",
                    "type": "Популярные"
                }
            ]
        }
    """.trimIndent()

    val productDetailJson =
            """
        {
            "id": "prod1",
            "collectionId": "products",
            "collectionName": "products",
            "created": "2026-01-01T00:00:00Z",
            "updated": "2026-01-01T00:00:00Z",
            "title": "Футболка",
            "description": "Хлопковая футболка",
            "price": 1500,
            "typeCloses": "Верх",
            "type": "Новинки",
            "approximateCost": "1500 ₽"
        }
    """.trimIndent()

    // ==================== CART ====================

    // ==================== КОРЗИНА ====================
    val cartItemJson =
            """
        {
            "id": "cart1",
            "collectionId": "cart",
            "collectionName": "cart",
            "created": "2026-01-01T00:00:00Z",
            "updated": "2026-01-01T00:00:00Z",
            "user_id": "user123",
            "product_id": "prod1",
            "count": 2
        }
    """.trimIndent()

    // ==================== PROJECTS ====================

    // ==================== ПРОЕКТЫ ====================
    val projectsListJson =
            """
        {
            "page": 1,
            "perPage": 30,
            "totalPages": 1,
            "totalItems": 1,
            "items": [
                {
                    "id": "proj1",
                    "collectionId": "project",
                    "collectionName": "project",
                    "created": "2026-01-01T00:00:00Z",
                    "updated": "2026-01-01T00:00:00Z",
                    "title": "Мой проект",
                    "dateStart": "2026-01-01",
                    "dateEnd": "2026-12-31",
                    "gender": "Мужской",
                    "description_source": "Описание",
                    "category": "Популярные",
                    "image": "image.jpg",
                    "user_id": "user123"
                }
            ]
        }
    """.trimIndent()

    val createdProjectJson =
            """
        {
            "id": "proj2",
            "collectionId": "project",
            "collectionName": "project",
            "created": "2026-01-02T00:00:00Z",
            "updated": "2026-01-02T00:00:00Z",
            "title": "Новый проект",
            "dateStart": "2026-02-01",
            "dateEnd": "2026-06-30",
            "gender": "Женский",
            "description_source": "Новое описание",
            "category": "Новинки",
            "image": "",
            "user_id": "user123"
        }
    """.trimIndent()

    // ==================== ORDER ====================

    // ==================== ЗАКАЗЫ ====================
    val orderJson =
            """
        {
            "id": "order1",
            "collectionId": "orders",
            "collectionName": "orders",
            "created": "2026-01-01T00:00:00Z",
            "updated": "2026-01-01T00:00:00Z",
            "user_id": "user123",
            "product_id": "prod1",
            "count": 3
        }
    """.trimIndent()

    // ==================== NEWS ====================

    // ==================== НОВОСТИ ====================
    val newsListJson =
            """
        {
            "page": 1,
            "perPage": 30,
            "totalPages": 1,
            "totalItems": 1,
            "items": [
                {
                    "id": "news1",
                    "collectionId": "news",
                    "collectionName": "news",
                    "newsImage": "promo.jpg",
                    "created": "2026-01-01T00:00:00Z",
                    "updated": "2026-01-01T00:00:00Z"
                }
            ]
        }
    """.trimIndent()

    // ==================== TEST DATA OBJECTS ====================

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param firstname Имя пользователя для сохранения или обновления профиля.
     * @param lastname Фамилия пользователя для сохранения или обновления профиля.
     */
    fun createTestUser(
            id: String = "user123",
            firstname: String = "Иван",
            lastname: String = "Иванов"
    ) =
            User(
                    id = id,
                    collectionId = "users",
                    collectionName = "_pb_users_auth_",
                    created = "2026-01-01T00:00:00Z",
                    updated = "2026-01-01T00:00:00Z",
                    emailVisibility = true,
                    firstname = firstname,
                    lastname = lastname,
                    secondname = "Иванович",
                    verified = true,
                    datebirthday = "1990-01-01",
                    gender = "Мужской"
            )

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param title Заголовок, который отображается в интерфейсе.
     * @param price Цена товара или заказа, используемая в расчете.
     */
    fun createTestProductItem(id: String = "prod1", title: String = "Футболка", price: Int = 1500) =
            ProductItem(
                    id = id,
                    title = title,
                    price = price,
                    typeCloses = "Верх",
                    type = "Новинки"
            )

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param title Заголовок, который отображается в интерфейсе.
     * @param price Цена товара или заказа, используемая в расчете.
     */
    fun createTestProductApi(id: String = "prod1", title: String = "Футболка", price: Int = 1500) =
            ProductApi(
                    id = id,
                    collectionId = "products",
                    collectionName = "products",
                    created = "2026-01-01T00:00:00Z",
                    updated = "2026-01-01T00:00:00Z",
                    title = title,
                    description = "Описание товара",
                    price = price,
                    typeCloses = "Верх",
                    type = "Новинки",
                    approximateCost = "$price ₽"
            )

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param title Заголовок, который отображается в интерфейсе.
     */
    fun createTestProjectApi(id: String = "proj1", title: String = "Мой проект") =
            ProjectApi(
                    id = id,
                    collectionId = "project",
                    collectionName = "project",
                    created = "2026-01-01T00:00:00Z",
                    updated = "2026-01-01T00:00:00Z",
                    title = title,
                    dateStart = "2026-01-01",
                    dateEnd = "2026-12-31",
                    gender = "Мужской",
                    descriptionSource = "Описание",
                    category = "Популярные",
                    image = "image.jpg",
                    userId = "user123"
            )

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    fun createTestResponseCart(id: String = "cart1", productId: String = "prod1", count: Int = 1) =
            ResponseCart(
                    id = id,
                    collectionId = "cart",
                    collectionName = "cart",
                    created = "2026-01-01T00:00:00Z",
                    updated = "2026-01-01T00:00:00Z",
                    userId = "user123",
                    productId = productId,
                    count = count
            )

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    fun createTestResponseOrder(
            id: String = "order1",
            productId: String = "prod1",
            count: Int = 1
    ) =
            ResponseOrder(
                    id = id,
                    collectionId = "orders",
                    collectionName = "orders",
                    created = "2026-01-01T00:00:00Z",
                    updated = "2026-01-01T00:00:00Z",
                    userId = "user123",
                    productId = productId,
                    count = count
            )

    // Создает новую сущность на основе переданных данных.
    fun createTestResponseAuth() = ResponseAuth(record = createTestUser(), token = "test_token_123")

    // Создает новую сущность на основе переданных данных.
    fun createTestResponseRegister() =
            ResponseRegister(
                    id = "user456",
                    collectionId = "users",
                    collectionName = "_pb_users_auth_",
                    created = "2026-01-02T00:00:00Z",
                    updated = "2026-01-02T00:00:00Z",
                    emailVisibility = true,
                    firstname = "",
                    lastname = "",
                    secondname = "",
                    verified = false,
                    datebirthday = "",
                    gender = ""
            )

    val updatedUserJson =
            """
        {
            "id": "user123",
            "collectionId": "users",
            "collectionName": "_pb_users_auth_",
            "created": "2026-01-01T00:00:00Z",
            "updated": "2026-01-01T00:00:00Z",
            "emailVisibility": true,
            "firstname": "Петр",
            "lastname": "Петров",
            "secondname": "Петрович",
            "verified": true,
            "datebirthday": "1995-05-15",
            "gender": "Мужской"
        }
    """.trimIndent()

    val usersAuthJson =
            """
        {
            "page": 1,
            "perPage": 30,
            "totalPages": 1,
            "totalItems": 1,
            "items": [
                {
                    "id": "token1",
                    "collectionId": "_authOrigins",
                    "collectionName": "_authOrigins",
                    "created": "2026-01-01T00:00:00Z",
                    "updated": "2026-01-01T00:00:00Z",
                    "collectionRef": "users",
                    "fingerprint": "fp_test",
                    "recordRef": "user123"
                }
            ]
        }
    """.trimIndent()
}

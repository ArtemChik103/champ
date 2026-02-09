package com.example.lol.data.network.api

import com.example.lol.data.network.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API для взаимодействия с backend Matule.
 *
 * Интерфейс описывает операции авторизации, профиля, каталога, проектов, корзины и заказов.
 */
// Определяет контракт сетевого API и доступные endpoint-операции.
interface MatuleApi {

        /**
         * Регистрирует нового пользователя.
         *
         * @param request Тело запроса на регистрацию.
         */
        /**
         * Регистрирует сущность и сохраняет результат операции в текущем состоянии.
         *
         * @param request Модель запроса с данными для отправки на сервер.
         */
        @POST("collections/users/records")
        suspend fun register(@Body request: RequestRegister): Response<okhttp3.ResponseBody>

        /**
         * Авторизует пользователя по email и паролю.
         *
         * @param request Тело запроса на авторизацию.
         */
        /**
         * Добавляет заголовок авторизации к запросу, если токен доступен в провайдере.
         *
         * @param request Модель запроса с данными для отправки на сервер.
         */
        @POST("collections/users/auth-with-password")
        suspend fun auth(@Body request: RequestAuth): retrofit2.Response<okhttp3.ResponseBody>

        /**
         * Возвращает профиль пользователя по идентификатору.
         *
         * @param userId Идентификатор пользователя.
         */
        /**
         * Возвращает актуальные данные из текущего источника состояния.
         *
         * @param userId Идентификатор пользователя, от имени которого выполняется операция.
         */
        @GET("collections/users/records/{id_user}")
        suspend fun getUser(@Path("id_user") userId: String): Response<User>

        /**
         * Обновляет профиль пользователя.
         *
         * @param userId Идентификатор пользователя.
         * @param email Новый email.
         * @param firstname Имя.
         * @param lastname Фамилия.
         * @param secondname Отчество.
         * @param datebirthday Дата рождения.
         * @param gender Пол.
         */
        /**
         * Обновляет существующую сущность и возвращает результат операции.
         *
         * @param userId Идентификатор пользователя, от имени которого выполняется операция.
         * @param email Email пользователя, используемый как идентификатор учетной записи.
         * @param firstname Имя пользователя для сохранения или обновления профиля.
         * @param lastname Фамилия пользователя для сохранения или обновления профиля.
         * @param secondname Отчество пользователя для заполнения профиля.
         * @param datebirthday Дата рождения в формате, ожидаемом сервером.
         * @param gender Выбранный пол пользователя или проекта.
         */
        @Multipart
        @PATCH("collections/users/records/{id_user}")
        suspend fun updateUser(
                @Path("id_user") userId: String,
                @Part("email") email: RequestBody? = null,
                @Part("firstname") firstname: RequestBody? = null,
                @Part("lastname") lastname: RequestBody? = null,
                @Part("secondname") secondname: RequestBody? = null,
                @Part("datebirthday") datebirthday: RequestBody? = null,
                @Part("gender") gender: RequestBody? = null
        ): Response<User>

        /**
         * Возвращает активные токены авторизации пользователя.
         */
        @GET("collections/_authOrigins/records") suspend fun getUsersAuth(): Response<UsersAuth>

        /**
         * Удаляет токен авторизации (выход из системы).
         *
         * @param tokenId Идентификатор токена в коллекции `_authOrigins`.
         */
        /**
         * Завершает пользовательскую сессию и очищает данные авторизации.
         *
         * @param tokenId Идентификатор токена для операций удаления или поиска.
         */
        @DELETE("collections/_authOrigins/records/{id_token}")
        suspend fun logout(@Path("id_token") tokenId: String): Response<Unit>

        /** Возвращает список новостей и акций. */
        @GET("collections/news/records") suspend fun getNews(): Response<ResponseNews>

        /**
         * Возвращает список продуктов.
         *
         * @param filter Фильтр в формате "(title ?~ 'query')"
         */
        /**
         * Возвращает актуальные данные из текущего источника состояния.
         *
         * @param filter Параметр фильтрации для запроса к API.
         */
        @GET("collections/products/records")
        suspend fun getProducts(@Query("filter") filter: String? = null): Response<ResponseProducts>

        /**
         * Возвращает детальную информацию о продукте.
         *
         * @param productId Идентификатор продукта.
         */
        /**
         * Возвращает актуальные данные из текущего источника состояния.
         *
         * @param productId Идентификатор товара для поиска или изменения записи.
         */
        @GET("collections/products/records/{id_product}")
        suspend fun getProduct(@Path("id_product") productId: String): Response<ProductApi>

        /** Возвращает список проектов текущего пользователя. */
        @GET("collections/project/records") suspend fun getProjects(): Response<ResponseProjects>

        /**
         * Создаёт новый проект.
         *
         * @param title Название проекта.
         * @param typeProject Тип проекта.
         * @param userId Идентификатор владельца проекта.
         * @param dateStart Дата начала.
         * @param dateEnd Дата окончания.
         * @param gender Пол целевой аудитории/пол пользователя.
         * @param descriptionSource Источник описания.
         * @param category Категория проекта.
         * @param image Необязательное изображение проекта.
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
        @Multipart
        @POST("collections/project/records")
        suspend fun createProject(
                @Part("title") title: RequestBody,
                @Part("typeProject") typeProject: RequestBody,
                @Part("user_id") userId: RequestBody,
                @Part("dateStart") dateStart: RequestBody,
                @Part("dateEnd") dateEnd: RequestBody,
                @Part("gender") gender: RequestBody,
                @Part("description_source") descriptionSource: RequestBody,
                @Part("category") category: RequestBody,
                @Part image: MultipartBody.Part? = null
        ): Response<ProjectApi>

        /**
         * Добавляет товар в корзину.
         *
         * @param request Тело запроса с данными корзины.
         */
        /**
         * Создает новую сущность на основе переданных данных.
         *
         * @param request Модель запроса с данными для отправки на сервер.
         */
        @POST("collections/cart/records")
        suspend fun createCartItem(@Body request: RequestCart): Response<ResponseCart>

        /**
         * Обновляет количество товара в корзине.
         *
         * @param cartItemId Идентификатор записи корзины.
         * @param userId Идентификатор пользователя.
         * @param productId Идентификатор товара.
         * @param count Новое количество.
         */
        /**
         * Обновляет существующую сущность и возвращает результат операции.
         *
         * @param cartItemId Идентификатор позиции корзины, которую нужно обновить.
         * @param userId Идентификатор пользователя, от имени которого выполняется операция.
         * @param productId Идентификатор товара для поиска или изменения записи.
         * @param count Количество элементов для установки или изменения.
         */
        @Multipart
        @PATCH("collections/cart/records/{id_bucket}")
        suspend fun updateCartItem(
                @Path("id_bucket") cartItemId: String,
                @Part("user_id") userId: RequestBody,
                @Part("product_id") productId: RequestBody,
                @Part("count") count: RequestBody
        ): Response<ResponseCart>

        /**
         * Создаёт заказ.
         *
         * @param request Тело запроса с параметрами заказа.
         */
        /**
         * Создает новую сущность на основе переданных данных.
         *
         * @param request Модель запроса с данными для отправки на сервер.
         */
        @POST("collections/orders/records")
        suspend fun createOrder(@Body request: RequestOrder): Response<ResponseOrder>
}

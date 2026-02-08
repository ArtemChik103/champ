package com.example.lol.data.network.api

import com.example.lol.data.network.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API интерфейс для взаимодействия с сервером Matule. Базовый URL: https://dsn-vypryamitel.ru/api
 *
 * Документация эндпоинтов согласно api.yaml спецификации.
 */
interface MatuleApi {

        // ==================== USER ====================

        /** Регистрация нового пользователя. POST /collections/users/records */
        @POST("collections/users/records")
        suspend fun register(@Body request: RequestRegister): Response<okhttp3.ResponseBody>

        /** Авторизация пользователя. POST /collections/users/auth-with-password */
        @POST("collections/users/auth-with-password")
        suspend fun auth(@Body request: RequestAuth): retrofit2.Response<okhttp3.ResponseBody>

        /** Получение информации о пользователе. GET /collections/users/records/{id_user} */
        @GET("collections/users/records/{id_user}")
        suspend fun getUser(@Path("id_user") userId: String): Response<User>

        /** Обновление профиля пользователя. PATCH /collections/users/records/{id_user} */
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
         * Получение списка токенов авторизации (для logout). GET /collections/_authOrigins/records
         */
        @GET("collections/_authOrigins/records") suspend fun getUsersAuth(): Response<UsersAuth>

        /**
         * Удаление токена (выход из системы). DELETE /collections/_authOrigins/records/{id_token}
         */
        @DELETE("collections/_authOrigins/records/{id_token}")
        suspend fun logout(@Path("id_token") tokenId: String): Response<Unit>

        // ==================== SHOP ====================

        /** Получение списка новостей/акций. GET /collections/news/records */
        @GET("collections/news/records") suspend fun getNews(): Response<ResponseNews>

        /**
         * Получение списка продуктов с опциональным фильтром поиска. GET
         * /collections/products/records
         * @param filter Фильтр в формате "(title ?~ 'query')"
         */
        @GET("collections/products/records")
        suspend fun getProducts(@Query("filter") filter: String? = null): Response<ResponseProducts>

        /**
         * Получение детальной информации о продукте. GET /collections/products/records/{id_product}
         */
        @GET("collections/products/records/{id_product}")
        suspend fun getProduct(@Path("id_product") productId: String): Response<ProductApi>

        // ==================== PROJECT ====================

        /** Получение списка проектов пользователя. GET /collections/project/records */
        @GET("collections/project/records") suspend fun getProjects(): Response<ResponseProjects>

        /** Создание нового проекта. POST /collections/project/records */
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

        // ==================== CART ====================

        /** Добавление товара в корзину. POST /collections/cart/records */
        @POST("collections/cart/records")
        suspend fun createCartItem(@Body request: RequestCart): Response<ResponseCart>

        /** Обновление количества товара в корзине. PATCH /collections/cart/records/{id_bucket} */
        @Multipart
        @PATCH("collections/cart/records/{id_bucket}")
        suspend fun updateCartItem(
                @Path("id_bucket") cartItemId: String,
                @Part("user_id") userId: RequestBody,
                @Part("product_id") productId: RequestBody,
                @Part("count") count: RequestBody
        ): Response<ResponseCart>

        // ==================== ORDER ====================

        /** Создание заказа. POST /collections/orders/records */
        @POST("collections/orders/records")
        suspend fun createOrder(@Body request: RequestOrder): Response<ResponseOrder>
}

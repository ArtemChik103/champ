package com.example.lol.data.network

import com.example.lol.data.network.api.MatuleApi
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton для создания и предоставления экземпляра Retrofit. Настраивает OkHttp клиент с
 * логированием и Bearer токеном.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://api.matule.ru/api/"
    private const val TIMEOUT_SECONDS = 30L

    private val gson = GsonBuilder().setLenient().create()

    private var tokenProvider: (() -> String?)? = null

    /**
     * Устанавливает провайдер токена для авторизации запросов.
     * @param provider Функция возвращающая текущий токен
     */
    fun setTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

    /** Interceptor для добавления Bearer токена в заголовки запросов. */
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = tokenProvider?.invoke()

        val request =
                if (!token.isNullOrBlank()) {
                    // Оставляем только печатные ASCII символы (0x20–0x7E)
                    val cleanToken = token.filter { it.code in 0x20..0x7E }
                    originalRequest
                            .newBuilder()
                            .header("Authorization", "Bearer $cleanToken")
                            .build()
                } else {
                    originalRequest
                }

        chain.proceed(request)
    }

    /** Interceptor для логирования HTTP запросов и ответов. */
    private val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    /** Настроенный OkHttp клиент с обходом SSL для демо. */
    private val okHttpClient: OkHttpClient by lazy {
        // Обход SSL проверки для сервера с проблемным сертификатом
        val trustAllCerts =
                arrayOf<javax.net.ssl.TrustManager>(
                        object : javax.net.ssl.X509TrustManager {
                            override fun checkClientTrusted(
                                    chain: Array<java.security.cert.X509Certificate>,
                                    authType: String
                            ) {}
                            override fun checkServerTrusted(
                                    chain: Array<java.security.cert.X509Certificate>,
                                    authType: String
                            ) {}
                            override fun getAcceptedIssuers():
                                    Array<java.security.cert.X509Certificate> = arrayOf()
                        }
                )

        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        OkHttpClient.Builder()
                .sslSocketFactory(
                        sslSocketFactory,
                        trustAllCerts[0] as javax.net.ssl.X509TrustManager
                )
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()
    }

    /** Экземпляр Retrofit. */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    /** API интерфейс для выполнения запросов. */
    val api: MatuleApi by lazy { retrofit.create(MatuleApi::class.java) }

    /**
     * Создаёт экземпляр Retrofit с кастомным базовым URL. Используется для тестирования с
     * MockWebServer.
     */
    fun createWithBaseUrl(baseUrl: String): MatuleApi {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(MatuleApi::class.java)
    }
}

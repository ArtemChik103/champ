package com.example.lol.authorization

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException

/**
 * Helper для сохранения учётных данных в Google Password Manager. Использует Android Credential
 * Manager API.
 */
// Определяет поведение и состояние компонента в рамках текущего модуля.
class CredentialHelper(context: Context) {
    private val credentialManager = CredentialManager.create(context)

    /**
     * Сохраняет email и пароль пользователя в Google Password Manager.
     * @param context Контекст (должен быть Activity)
     * @param email Email пользователя (используется как ID)
     * @param password Пароль пользователя
     * @return true если сохранено успешно, false если пользователь отменил или произошла ошибка
     */
    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param context Контекст Android для доступа к системным сервисам и ресурсам.
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    suspend fun saveCredentials(context: Context, email: String, password: String): Boolean {
        return try {
            val request = CreatePasswordRequest(id = email, password = password)
            credentialManager.createCredential(context = context, request = request)
            true
        } catch (e: CreateCredentialCancellationException) {
            // Пользователь отменил сохранение - это нормально
            false
        } catch (e: CreateCredentialException) {
            // Другая ошибка - логируем, но не показываем пользователю
            false
        } catch (e: Exception) {
            // Общая ошибка
            false
        }
    }
}

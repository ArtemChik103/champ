package com.example.lol.authorization

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
            context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CURRENT_USER_EMAIL = "current_user_email"
        private const val KEY_USERS_LIST = "registered_users"
        private const val KEY_LAST_ROUTE = "last_route"
        private const val KEY_ONE_SHOT_INACTIVITY_SENT = "one_shot_inactivity_sent"

        // Шаблоны ключей для данных конкретного пользователя.
        private fun keyPassword(email: String) = "user:$email:password"
        private fun keyPin(email: String) = "user:$email:pin"
        private fun keyName(email: String) = "user:$email:name"

        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_PROFILE_DRAFT_NAME = "profile_draft_name"
        private const val KEY_PROFILE_DRAFT_SURNAME = "profile_draft_surname"
        private const val KEY_PROFILE_DRAFT_PATRONYMIC = "profile_draft_patronymic"
        private const val KEY_PROFILE_DRAFT_BIRTHDAY = "profile_draft_birthday"
        private const val KEY_PROFILE_DRAFT_GENDER = "profile_draft_gender"
        private const val KEY_PROFILE_DRAFT_EMAIL = "profile_draft_email"
    }

    data class CreateProfileDraft(
            val name: String = "",
            val surname: String = "",
            val patronymic: String = "",
            val birthday: String = "",
            val gender: String = "",
            val email: String = ""
    )

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    fun markOneShotInactivityNotificationSent() {
        prefs.edit().putBoolean(KEY_ONE_SHOT_INACTIVITY_SENT, true).apply()
    }

    fun isOneShotInactivityNotificationSent(): Boolean {
        return prefs.getBoolean(KEY_ONE_SHOT_INACTIVITY_SENT, false)
    }

    fun resetOneShotInactivityNotificationCycle() {
        prefs.edit().putBoolean(KEY_ONE_SHOT_INACTIVITY_SENT, false).apply()
    }

    fun saveCreateProfileDraft(
            name: String,
            surname: String,
            patronymic: String,
            birthday: String,
            gender: String,
            email: String
    ) {
        prefs.edit()
                .putString(KEY_PROFILE_DRAFT_NAME, name)
                .putString(KEY_PROFILE_DRAFT_SURNAME, surname)
                .putString(KEY_PROFILE_DRAFT_PATRONYMIC, patronymic)
                .putString(KEY_PROFILE_DRAFT_BIRTHDAY, birthday)
                .putString(KEY_PROFILE_DRAFT_GENDER, gender)
                .putString(KEY_PROFILE_DRAFT_EMAIL, email)
                .apply()
    }

    fun getCreateProfileDraft(): CreateProfileDraft {
        return CreateProfileDraft(
                name = prefs.getString(KEY_PROFILE_DRAFT_NAME, "").orEmpty(),
                surname = prefs.getString(KEY_PROFILE_DRAFT_SURNAME, "").orEmpty(),
                patronymic = prefs.getString(KEY_PROFILE_DRAFT_PATRONYMIC, "").orEmpty(),
                birthday = prefs.getString(KEY_PROFILE_DRAFT_BIRTHDAY, "").orEmpty(),
                gender = prefs.getString(KEY_PROFILE_DRAFT_GENDER, "").orEmpty(),
                email = prefs.getString(KEY_PROFILE_DRAFT_EMAIL, "").orEmpty()
        )
    }

    fun clearCreateProfileDraft() {
        prefs.edit()
                .remove(KEY_PROFILE_DRAFT_NAME)
                .remove(KEY_PROFILE_DRAFT_SURNAME)
                .remove(KEY_PROFILE_DRAFT_PATRONYMIC)
                .remove(KEY_PROFILE_DRAFT_BIRTHDAY)
                .remove(KEY_PROFILE_DRAFT_GENDER)
                .remove(KEY_PROFILE_DRAFT_EMAIL)
                .apply()
    }

    fun saveLastRoute(route: String) {
        prefs.edit().putString(KEY_LAST_ROUTE, route).apply()
    }

    fun getLastRoute(): String {
        val route = prefs.getString(KEY_LAST_ROUTE, "Main") ?: "Main"
        val validStartRoutes =
                setOf(
                        "Main",
                        "Catalogue",
                        "Projects",
                        "Profile",
                        "Cart",
                        "MyOrders",
                        "CreateProject"
                )

        return when {
            route in validStartRoutes -> route
            route.startsWith("OrderDetails/") -> route
            route.startsWith("ProjectDetails/") -> route
            else -> "Main"
        }
    }

    fun isUserExists(email: String): Boolean {
        val users = prefs.getStringSet(KEY_USERS_LIST, emptySet()) ?: emptySet()
        return users.contains(email)
    }

    fun registerUser(email: String, name: String) {
        val users = prefs.getStringSet(KEY_USERS_LIST, emptySet())?.toMutableSet() ?: mutableSetOf()
        users.add(email)
        prefs.edit().putStringSet(KEY_USERS_LIST, users).putString(keyName(email), name).apply()
    }

    fun savePassword(email: String, password: String) {
        prefs.edit().putString(keyPassword(email), password).apply()
    }

    fun savePin(email: String, pin: String) {
        prefs.edit().putString(keyPin(email), pin).apply()
    }

    fun validatePassword(email: String, password: String): Boolean {
        val savedPassword = prefs.getString(keyPassword(email), null)
        return savedPassword == password
    }

    fun getUserPin(email: String): String? {
        return prefs.getString(keyPin(email), null)
    }

    fun getUserName(email: String): String? {
        return prefs.getString(keyName(email), null)
    }

    fun setCurrentEmail(email: String) {
        prefs.edit().putString(KEY_CURRENT_USER_EMAIL, email).apply()
    }

    fun getCurrentEmail(): String? {
        return prefs.getString(KEY_CURRENT_USER_EMAIL, null)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        prefs.edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_CURRENT_USER_EMAIL)
                .remove(KEY_LAST_ROUTE)
                .remove(KEY_ONE_SHOT_INACTIVITY_SENT)
                .remove(KEY_PROFILE_DRAFT_NAME)
                .remove(KEY_PROFILE_DRAFT_SURNAME)
                .remove(KEY_PROFILE_DRAFT_PATRONYMIC)
                .remove(KEY_PROFILE_DRAFT_BIRTHDAY)
                .remove(KEY_PROFILE_DRAFT_GENDER)
                .remove(KEY_PROFILE_DRAFT_EMAIL)
                .apply()
    }

    // Методы сохранены для совместимости со старым кодом и проксируют текущего пользователя.
    fun getEmail(): String? = getCurrentEmail()
    fun getUserName(): String? = getCurrentEmail()?.let { getUserName(it) } ?: "Эдуард"
    fun getPin(): String? = getCurrentEmail()?.let { getUserPin(it) }
}

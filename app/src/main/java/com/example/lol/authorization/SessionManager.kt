package com.example.lol.authorization

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_USER_PIN = "user_pin"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_LAST_ROUTE = "last_route"
    }

    fun saveLastRoute(route: String) {
        prefs.edit().putString(KEY_LAST_ROUTE, route).apply()
    }

    fun getLastRoute(): String {
        return prefs.getString(KEY_LAST_ROUTE, "Main") ?: "Main"
    }

    fun saveEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun savePassword(password: String) {
        prefs.edit().putString(KEY_USER_PASSWORD, password).apply()
    }

    fun savePin(pin: String) {
        prefs.edit().putString(KEY_USER_PIN, pin).apply()
    }

    fun saveUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getPassword(): String? {
        return prefs.getString(KEY_USER_PASSWORD, null)
    }

    fun getPin(): String? {
        return prefs.getString(KEY_USER_PIN, null)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, "Эдуард") // Default to what was in the UI if not set
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
    
    fun fetchUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
}

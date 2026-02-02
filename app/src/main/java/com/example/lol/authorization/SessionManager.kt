package com.example.lol.authorization

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
            context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CURRENT_USER_EMAIL = "current_user_email"
        private const val KEY_USERS_LIST = "registered_users" // StringSet of emails
        private const val KEY_LAST_ROUTE = "last_route"

        // Multi-user key templates
        private fun keyPassword(email: String) = "user:$email:password"
        private fun keyPin(email: String) = "user:$email:pin"
        private fun keyName(email: String) = "user:$email:name"
    }

    fun saveLastRoute(route: String) {
        prefs.edit().putString(KEY_LAST_ROUTE, route).apply()
    }

    fun getLastRoute(): String {
        val route = prefs.getString(KEY_LAST_ROUTE, "Main") ?: "Main"
        // Valid start destinations (no arguments)
        // Valid start destinations
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

    // --- Mock Database Logic ---

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

    // --- Current Session Logic ---

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
                .apply()
    }

    // Keep legacy methods for backward compatibility if needed, but point to current user
    fun getEmail(): String? = getCurrentEmail()
    fun getUserName(): String? = getCurrentEmail()?.let { getUserName(it) } ?: "Эдуард"
    fun getPin(): String? = getCurrentEmail()?.let { getUserPin(it) }
}

package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseAuth
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.network.models.User
import java.util.UUID

class MockAuthRepository : IAuthRepository {

        companion object {
                val instance: MockAuthRepository by lazy { MockAuthRepository() }
        }

        private val users = mutableMapOf<String, MockUser>()
        private var currentUser: MockUser? = null

        data class MockUser(
                val id: String,
                val email: String,
                val password: String,
                var firstname: String = "",
                var lastname: String = "",
                var secondname: String = "",
                var datebirthday: String = "",
                var gender: String = ""
        )

        override suspend fun register(
                email: String,
                password: String
        ): NetworkResult<ResponseRegister> {
                if (users.containsKey(email)) {
                        return NetworkResult.Error("Пользователь уже существует")
                }
                val id = UUID.randomUUID().toString()
                val created = "2023-10-27 10:00:00.000Z"

                users[email] = MockUser(id = id, email = email, password = password)

                return NetworkResult.Success(
                        ResponseRegister(
                                id = id,
                                collectionId = "users_collection",
                                collectionName = "users",
                                created = created,
                                updated = created,
                                emailVisibility = true,
                                firstname = "",
                                lastname = "",
                                secondname = "",
                                verified = true,
                                datebirthday = "",
                                gender = ""
                        )
                )
        }

        override suspend fun login(email: String, password: String): NetworkResult<ResponseAuth> {
                val user = users[email]
                if (user == null || user.password != password) {
                        return NetworkResult.Error("Неверный email или пароль")
                }

                currentUser = user

                val userModel =
                        User(
                                id = user.id,
                                collectionId = "users_collection",
                                collectionName = "users",
                                created = "2023-10-27 10:00:00.000Z",
                                updated = "2023-10-27 10:00:00.000Z",
                                emailVisibility = true,
                                firstname = user.firstname,
                                lastname = user.lastname,
                                secondname = user.secondname,
                                verified = true,
                                datebirthday = user.datebirthday,
                                gender = user.gender
                        )

                return NetworkResult.Success(
                        ResponseAuth(record = userModel, token = "mock_token_${user.id}")
                )
        }

        override suspend fun getUser(userId: String): NetworkResult<User> {
                val user = currentUser ?: users.values.find { it.id == userId }

                if (user == null) {
                        return NetworkResult.Error("Пользователь не найден")
                }

                return NetworkResult.Success(
                        User(
                                id = user.id,
                                collectionId = "users_collection",
                                collectionName = "users",
                                created = "2023-10-27 10:00:00.000Z",
                                updated = "2023-10-27 10:00:00.000Z",
                                emailVisibility = true,
                                firstname = user.firstname,
                                lastname = user.lastname,
                                secondname = user.secondname,
                                verified = true,
                                datebirthday = user.datebirthday,
                                gender = user.gender
                        )
                )
        }

        override suspend fun updateUser(
                userId: String,
                firstname: String?,
                lastname: String?,
                secondname: String?,
                datebirthday: String?,
                gender: String?
        ): NetworkResult<User> {
                val user = currentUser ?: users.values.find { it.id == userId }

                if (user == null) {
                        return NetworkResult.Error("Пользователь не найден")
                }

                firstname?.let { user.firstname = it }
                lastname?.let { user.lastname = it }
                secondname?.let { user.secondname = it }
                datebirthday?.let { user.datebirthday = it }
                gender?.let { user.gender = it }

                users[user.email] = user

                return NetworkResult.Success(
                        User(
                                id = user.id,
                                collectionId = "users_collection",
                                collectionName = "users",
                                created = "2023-10-27 10:00:00.000Z",
                                updated = "2023-10-27 10:00:00.000Z",
                                emailVisibility = true,
                                firstname = user.firstname,
                                lastname = user.lastname,
                                secondname = user.secondname,
                                verified = true,
                                datebirthday = user.datebirthday,
                                gender = user.gender
                        )
                )
        }

        override suspend fun logout(): NetworkResult<Unit> {
                currentUser = null
                return NetworkResult.Success(Unit)
        }
}

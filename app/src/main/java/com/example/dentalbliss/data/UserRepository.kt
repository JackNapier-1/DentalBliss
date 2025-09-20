package com.example.dentalbliss.data

import com.example.dentalbliss.ui.screens.UserRole

data class User(
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val role: UserRole
)

object UserRepository {
    val users = mutableListOf<User>()

    fun registerUser(user: User) {
        users.add(user)
    }

    fun validateCredentials(email: String, password: String, role: UserRole): Boolean {
        return users.any { it.email == email && it.password == password && it.role == role }
    }

    fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
} 
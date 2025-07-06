package com.example.trabalho.repository

import com.example.trabalho.DAO.UserDao
import com.example.trabalho.data.UserEntity
import java.security.MessageDigest
import java.util.UUID

class UserRepository(private val userDao: UserDao) {
    suspend fun getAllUsers() = userDao.getAllUsers()

    suspend fun getUserById(id: String) = userDao.getUserById(id)

    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    suspend fun createUser(user: UserEntity) = userDao.createUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    suspend fun createAdminIfNotExists() {
        val adminEmail = "admin@admin.com"
        val existingAdmin = getUserByEmail(adminEmail)

        if (existingAdmin == null) {
            val passwordPlain = "admin123"
            val passwordHash = passwordPlain.sha256()

            val admin = UserEntity(
                id = UUID.randomUUID().toString(),
                nome = "Administrator",
                email = adminEmail,
                password = passwordHash,
                role = "ADMINISTRADOR",
                urlFoto = null
            )
            createUser(admin)
        }
    }
    private fun String.sha256() = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .joinToString("") { "%02x".format(it) }
}
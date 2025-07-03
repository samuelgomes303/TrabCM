package com.example.trabalho.repository

import com.example.trabalho.DAO.UserDao
import com.example.trabalho.data.UserEntity
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
            val admin = UserEntity(
                id = UUID.randomUUID().toString(),
                nome = "Administrator",
                email = adminEmail,
                password = "admin123", // In production, use a secure hash
                role = "ADMINISTRADOR",
                urlFoto = null
            )
            createUser(admin)
        }
    }
}
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

    suspend fun seedDefaultUsers() {
        fun hash(pwd: String) = MessageDigest.getInstance("SHA-256")
            .digest(pwd.toByteArray())
            .joinToString("") { "%02x".format(it) }

        // (1) Administrador
        ensureUser(
            email = "admin@admin.com",
            nome  = "Administrator",
            role  = "ADMINISTRADOR",
            pwd   = "admin123"
        )

        // (2) Gestor de Projeto
        ensureUser(
            email = "gestor@exemplo.com",
            nome  = "Gestor Exemplo",
            role  = "GESTOR_PROJETO",
            pwd   = "gestor123"
        )

        // (3) Utilizador normal
        ensureUser(
            email = "user@exemplo.com",
            nome  = "Utilizador Exemplo",
            role  = "UTILIZADOR",
            pwd   = "user123"
        )
    }

    /** Se o e‑mail ainda não existir, cria-o com hash SHA‑256 da password. */
    private suspend fun ensureUser(
        email: String,
        nome: String,
        role: String,
        pwd: String,
        foto: String? = null
    ) {
        if (userDao.getUserByEmail(email) == null) {
            val user = UserEntity(
                id       = UUID.randomUUID().toString(),
                nome     = nome,
                email    = email,
                password = pwd.sha256(),
                role     = role,
                urlFoto  = foto
            )
            userDao.createUser(user)
        }
    }

    private fun String.sha256() = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .joinToString("") { "%02x".format(it) }
}
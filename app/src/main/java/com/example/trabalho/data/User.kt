package com.example.trabalho.data
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val nome: String,
    val email: String,
    val password: String,
    val role: String, // "ADMINISTRADOR", "GESTOR_PROJETO", "UTILIZADOR"
    val urlFoto: String?
)

package com.example.trabalho.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["idGestor"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val nome: String,
    val description: String,
    val idGestor: String,
    val startDate: Long,
    val endDate: Long?,
    val state: String // "EM_PROGRESSO", "CONCLUIDO", "CANCELADO"
)
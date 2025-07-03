package com.example.trabalho.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["idProject"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val idProject: String,
    val title: String,
    val description: String,
    val startDate: Long,
    val finishDate: Long?,// data do dia final do projeto
    val finishPerc: Float,
    val state: String, // "A_FAZER", "EM_PROGRESSO", "CONCLUIDO"
    val local: String?,
    val timeSpent: Long
)
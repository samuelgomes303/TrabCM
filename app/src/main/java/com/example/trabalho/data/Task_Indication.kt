package com.example.trabalho.data
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "task_indication",
    primaryKeys = ["idTask", "idUser"],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["idTask"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["idUser"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskIndicationEntity(
    val idTask: String,
    val idUser: String,
    val indicationDate: Long
)

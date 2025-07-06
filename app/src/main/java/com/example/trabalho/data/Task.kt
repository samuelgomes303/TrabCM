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
    val finishDate: Long?,
    val finishPerc: Float,
    val state: String,
    val local: String?,
    val timeSpent: Long
)
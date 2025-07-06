package com.example.trabalho.data
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "project_user",
    primaryKeys = ["projectId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Project_User(
    val projectId: String,
    val userId: String
)

package com.example.trabalho.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.trabalho.data.Project_User
import com.example.trabalho.data.UserEntity

@Dao
interface ProjectUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserToProject(ref: Project_User)

    @Query("SELECT * FROM users INNER JOIN project_user ON users.id = project_user.userId WHERE project_user.projectId = :projectId")
    suspend fun getUsersForProject(projectId: String): List<UserEntity>

    @Query("DELETE FROM project_user WHERE projectId = :projectId AND userId = :userId")
    suspend fun removeUserFromProject(projectId: String, userId: String)
}

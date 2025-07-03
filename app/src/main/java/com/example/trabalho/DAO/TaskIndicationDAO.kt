package com.example.trabalho.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trabalho.data.TaskIndicationEntity

@Dao
interface TaskIndicationDao {
    @Query("SELECT * FROM task_indication WHERE idTask = :taskId")
    suspend fun getAssignmentsByTask(taskId: String): List<TaskIndicationEntity>

    @Query("SELECT * FROM task_indication WHERE idUser = :userId")
    suspend fun getAssignmentsByUser(userId: String): List<TaskIndicationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAssignment(assignment: TaskIndicationEntity)

    @Delete
    suspend fun deleteAssignment(assignment: TaskIndicationEntity)
}
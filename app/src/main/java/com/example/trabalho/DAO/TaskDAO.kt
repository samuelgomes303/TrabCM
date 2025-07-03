package com.example.trabalho.DAO
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trabalho.data.TaskEntity


@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE idProject = :projectId")
    suspend fun getTasksByProject(projectId: String): List<TaskEntity>

    @Query("""
        SELECT tasks.* FROM tasks 
        INNER JOIN task_indication ON tasks.id = task_indication.idTask
        WHERE task_indication.idUser = :userId
    """)
    suspend fun getTasksByUser(userId: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}

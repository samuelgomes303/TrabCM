package com.example.trabalho.repository

import com.example.trabalho.DAO.TaskDao
import com.example.trabalho.data.TaskEntity
import java.util.UUID

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun getTasksByProject(projectId: String) = taskDao.getTasksByProject(projectId)

    suspend fun getTasksByUser(userId: String) = taskDao.getTasksByUser(userId)

    suspend fun getTaskById(taskId: String) = taskDao.getTaskById(taskId)

    suspend fun createTask(task: TaskEntity) = taskDao.createTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun createNewTask(
        projectId: String,
        title: String,
        description: String,
        startDate: Long = System.currentTimeMillis(),
        finishDate: Long? = null,
        local: String? = null
    ): TaskEntity {
        val task = TaskEntity(
            id = UUID.randomUUID().toString(),
            idProject = projectId,
            title = title,
            description = description,
            startDate = startDate,
            finishDate = finishDate,
            finishPerc = 0f,
            state = "A_FAZER",
            local = local,
            timeSpent = 0
        )
        createTask(task)
        return task
    }

    suspend fun updateTaskProgress(taskId: String, newPercentage: Float) {
        getTaskById(taskId)?.let { task ->
            val updatedTask = task.copy(
                finishPerc = newPercentage.coerceIn(0f, 100f),
                state = when {
                    newPercentage >= 100f -> "CONCLUIDO"
                    newPercentage > 0f -> "EM_PROGRESSO"
                    else -> "A_FAZER"
                }
            )
            updateTask(updatedTask)
        }
    }
}
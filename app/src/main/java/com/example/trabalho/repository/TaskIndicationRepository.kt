package com.example.trabalho.repository

import com.example.trabalho.DAO.TaskIndicationDao
import com.example.trabalho.data.TaskIndicationEntity

class TaskIndicationRepository(private val taskIndicationDao: TaskIndicationDao) {
    suspend fun getAssignmentsByTask(taskId: String) = taskIndicationDao.getAssignmentsByTask(taskId)

    suspend fun getAssignmentsByUser(userId: String) = taskIndicationDao.getAssignmentsByUser(userId)

    suspend fun createAssignment(assignment: TaskIndicationEntity) = taskIndicationDao.createAssignment(assignment)

    suspend fun getProjectsByUser(userId: String) = taskIndicationDao.getProjectsByUser(userId)

    suspend fun deleteAssignment(assignment: TaskIndicationEntity) = taskIndicationDao.deleteAssignment(assignment)

    suspend fun assignTaskToUser(taskId: String, userId: String) {
        val assignment = TaskIndicationEntity(
            idTask = taskId,
            idUser = userId,
            indicationDate = System.currentTimeMillis()
        )
        createAssignment(assignment)
    }
}
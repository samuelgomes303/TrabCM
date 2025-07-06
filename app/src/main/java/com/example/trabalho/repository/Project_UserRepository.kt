package com.example.trabalho.repository

import com.example.trabalho.DAO.ProjectUserDao
import com.example.trabalho.data.Project_User
import com.example.trabalho.data.UserEntity

class ProjectUserRepository(private val dao: ProjectUserDao) {

    suspend fun addUserToProject(userId: String, projectId: String) {
        dao.addUserToProject(Project_User(projectId, userId))
    }

    suspend fun getUsersForProject(projectId: String): List<UserEntity> {
        return dao.getUsersForProject(projectId)
    }

    suspend fun removeUserFromProject(userId: String, projectId: String) {
        dao.removeUserFromProject(projectId, userId)
    }
}

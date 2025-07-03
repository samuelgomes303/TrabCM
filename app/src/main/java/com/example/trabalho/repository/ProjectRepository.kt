package com.example.trabalho.repository

import com.example.trabalho.DAO.ProjectDao
import com.example.trabalho.data.ProjectEntity
import java.util.UUID

class ProjectRepository(private val projectDao: ProjectDao) {
    suspend fun getAllProjects() = projectDao.getAllProjects()

    suspend fun getProjectById(id: String) = projectDao.getProjectById(id)

    suspend fun getProjectsByManager(gestorId: String) = projectDao.getProjectsByManager(gestorId)

    suspend fun createProject(project: ProjectEntity) = projectDao.createProject(project)

    suspend fun updateProject(project: ProjectEntity) = projectDao.updateProject(project)

    suspend fun deleteProject(project: ProjectEntity) = projectDao.deleteProject(project)

    suspend fun createNewProject(
        nome: String,
        description: String,
        idGestor: String,
        startDate: Long = System.currentTimeMillis(),
        endDate: Long? = null
    ): ProjectEntity {
        val project = ProjectEntity(
            id = UUID.randomUUID().toString(),
            nome = nome,
            description = description,
            idGestor = idGestor,
            startDate = startDate,
            endDate = endDate,
            state = "EM_PROGRESSO"
        )
        createProject(project)
        return project
    }
}
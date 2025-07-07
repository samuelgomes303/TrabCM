package com.example.trabalho.util

import ProjetoGestaoViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalho.DAO.ProjectDao
import com.example.trabalho.repository.UserRepository

class ProjetoGestaoViewModelFactory(
    private val projectDao: ProjectDao,
    private val userRepo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjetoGestaoViewModel::class.java)) {
            return ProjetoGestaoViewModel(projectDao, userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


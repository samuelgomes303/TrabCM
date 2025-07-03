package com.example.trabalho.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.repository.TaskIndicationRepository
import com.example.trabalho.repository.TaskRepository
import com.example.trabalho.viewModel.TaskViewModel

class TaskViewModelFactory( private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = BaseDados.getInstance(context)
        val taskRepo = TaskRepository(db.taskDao())
        val taskIndicationRepo = TaskIndicationRepository(db.taskIndicationDao())
        val app = context.applicationContext as Application

        @Suppress("UNCHECKED_CAST")
        return TaskViewModel(taskIndicationRepo, taskRepo, app) as T
    }
}

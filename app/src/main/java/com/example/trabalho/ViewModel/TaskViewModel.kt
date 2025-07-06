package com.example.trabalho.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho.data.TaskEntity
import com.example.trabalho.repository.TaskIndicationRepository
import com.example.trabalho.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel( private val taskIndicationRepo: TaskIndicationRepository, private val taskRepo: TaskRepository, app: Application): AndroidViewModel(app) {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks

    fun carregarTarefasDoUtilizador(userId: String) {
        viewModelScope.launch {
            try {
                //tasks indicadas para o user
                val assignments = taskIndicationRepo.getAssignmentsByUser(userId)

                val tasks = assignments.mapNotNull { assignment ->
                    taskRepo.getTaskById(assignment.idTask)
                }

                _tasks.value = tasks

            } catch (e: Exception) {
                _tasks.value = emptyList()
            }
        }
    }
}

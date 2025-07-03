package com.example.trabalho.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.repository.UserRepository
import com.example.trabalho.viewModel.RegistoViewModel

class RegistoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db  = BaseDados.getInstance(context)
        val repo = UserRepository(db.userDao())
        @Suppress("UNCHECKED_CAST")
        return RegistoViewModel(repo, context.applicationContext as Application) as T
    }
}
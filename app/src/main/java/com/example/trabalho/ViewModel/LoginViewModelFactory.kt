package com.example.trabalho.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.repository.UserRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db   = BaseDados.getInstance(context)
        val repo = UserRepository(db.userDao())
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(repo, context.applicationContext as Application) as T
    }
}

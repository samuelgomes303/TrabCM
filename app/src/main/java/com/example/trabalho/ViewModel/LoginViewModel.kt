package com.example.trabalho.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho.data.UserEntity
import com.example.trabalho.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginViewModel(private val repo: UserRepository, private val app: Application) : AndroidViewModel(app) {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    sealed class LoginState {
        object Initial : LoginState()
        object Loading : LoginState()
        data class Success(val user: UserEntity) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // Validações básicas
                when {
                    email.isBlank() -> {
                        _loginState.value = LoginState.Error("Email é obrigatório")
                        return@launch
                    }
                    !isValidEmail(email) -> {
                        _loginState.value = LoginState.Error("Email inválido")
                        return@launch
                    }
                    password.isBlank() -> {
                        _loginState.value = LoginState.Error("Senha é obrigatória")
                        return@launch
                    }
                }

                val user = repo.getUserByEmail(email)
                val senhaHash = password.sha256()
                if (user == null || user.password != senhaHash) {
                    _loginState.value = LoginState.Error("Credenciais inválidas")
                    return@launch
                }
                _loginState.value = LoginState.Success(user)

            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Erro ao fazer login: ${e.message}")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun String.sha256() = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .joinToString("") { "%02x".format(it) }
}
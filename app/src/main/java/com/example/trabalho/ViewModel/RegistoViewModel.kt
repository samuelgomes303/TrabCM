package com.example.trabalho.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho.data.UserEntity
import com.example.trabalho.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class RegistoViewModel(private val repo: UserRepository, private val app: Application) : AndroidViewModel(app) {

    private val _registoState = MutableStateFlow<RegistoState>(RegistoState.Initial)
    val registoState: StateFlow<RegistoState> = _registoState

    sealed class RegistoState {
        object Initial : RegistoState()
        object Loading : RegistoState()
        object Success : RegistoState()
        data class Error(val message: String) : RegistoState()
    }

    fun registar(nome: String, email: String, senha: String, confirmarSenha: String, fotoUri: Uri?) {
        viewModelScope.launch {

                _registoState.value = RegistoState.Loading

                val msgErro = when {
                    nome.isBlank() -> "Nome é obrigatório"
                    email.isBlank() -> "Email é obrigatório"
                    !isValidEmail(email) -> "Email inválido"
                    senha.isBlank() -> "Senha é obrigatória"
                    senha != confirmarSenha -> "As senhas não coincidem"
                    repo.getUserByEmail(email) != null -> "E‑mail já registado"
                    else -> null
                }
                msgErro?.let {
                    _registoState.value = RegistoState.Error(it)
                    return@launch
                }

                /* ---------- “Salvar” a foto ---------- */
                val fotoGuardada: String = when (fotoUri) {
                    null -> "SEM_FOTO"                          // <‑‑ valor fixo quando não há imagem
                    else -> fotoUri.toString()                  // ou copia para interno, se preferires
                }

                /* 3) (Opcional) Hash da senha */
                val senhaHash = senha.sha256()

                /* 4) Criar entidade e gravar */
                val userEntity = UserEntity(
                    id   = UUID.randomUUID().toString(),
                    nome = nome,
                    email = email,
                    password = senhaHash,
                    role = "UTILIZADOR",
                    urlFoto = fotoGuardada
                )
                try {
                    repo.createUser(userEntity)
                    _registoState.value = RegistoState.Success
                } catch (e: Exception) {
                    _registoState.value = RegistoState.Error("Erro ao gravar: ${e.message}")
                }
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun String.sha256(): String =
        MessageDigest.getInstance("SHA-256")
            .digest(toByteArray())
            .joinToString("") { "%02x".format(it) }

}
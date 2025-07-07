package com.example.trabalho.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.databinding.ActivityLoginBinding
import com.example.trabalho.repository.UserRepository
import com.example.trabalho.viewModel.LoginViewModel
import com.example.trabalho.viewModel.LoginViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels{
        LoginViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val db = BaseDados.getInstance(this@LoginActivity)
            val userRepo = UserRepository(db.userDao())
            userRepo.seedDefaultUsers()
        }
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editSenha.text.toString()
            viewModel.login(email, password)
        }

        binding.txtCriarConta.setOnClickListener {
            startActivity(Intent(this, RegistoActivity::class.java))
        }
    }

    private fun observeViewModel() { lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Initial -> { binding.btnLogin.isEnabled = true
                    }
                    is LoginViewModel.LoginState.Loading -> { binding.btnLogin.isEnabled = false
                    }

                    is LoginViewModel.LoginState.Success -> {
                        val user = state.user
                        val next = when (user.role.uppercase()) {
                            "ADMINISTRADOR"   -> MainPageActivity::class.java
                            "GESTOR_PROJETO"  -> MainPageActivity::class.java
                            else              -> MainPageActivity::class.java
                        }
                        val intent = Intent(this@LoginActivity, MainPageActivity::class.java).apply {
                            putExtra("ROLE", user.role)
                            putExtra("ID", user.id)
                        }
                        startActivity(intent)
                        finish()
                    }

                    is LoginViewModel.LoginState.Error -> {
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
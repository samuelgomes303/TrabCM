package com.example.trabalho.auth

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trabalho.ViewModel.RegistoViewModelFactory
import com.example.trabalho.databinding.ActivityRegistoBinding
import com.example.trabalho.viewModel.RegistoViewModel
import kotlinx.coroutines.launch

class RegistoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistoBinding
    private val viewModel: RegistoViewModel by viewModels(){
        RegistoViewModelFactory(this)
    }
    private var selectedImageUri: Uri? = null

    // Registro para seleção de imagem
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.imagemPerfil.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSelecionarFoto.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btnRegistar.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val confirmarSenha = binding.editConfirmarSenha.text.toString()

            viewModel.registar(nome, email, senha, confirmarSenha, selectedImageUri)
        }

        binding.txtJaTemConta.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.registoState.collect { state ->
                when (state) {
                    is RegistoViewModel.RegistoState.Initial -> {
                        binding.btnRegistar.isEnabled = true
                    }
                    is RegistoViewModel.RegistoState.Loading -> {
                        binding.btnRegistar.isEnabled = false
                    }
                    is RegistoViewModel.RegistoState.Success -> {
                        Toast.makeText(this@RegistoActivity, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is RegistoViewModel.RegistoState.Error -> {
                        binding.btnRegistar.isEnabled = true
                        Toast.makeText(this@RegistoActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
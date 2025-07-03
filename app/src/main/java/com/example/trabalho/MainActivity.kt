package com.example.trabalho

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho.auth.LoginActivity
import com.example.trabalho.auth.RegistoActivity
import com.example.trabalho.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar LoginFragment imediatamente
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finaliza MainActivity se não for necessária após login
    }

    fun navigateToRegisto() {
        val intent = Intent(this, RegistoActivity::class.java)
        startActivity(intent)
    }
}
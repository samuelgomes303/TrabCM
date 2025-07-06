package com.example.trabalho.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho.R
import com.example.trabalho.databinding.ActivityMainPageBinding
import com.example.trabalho.mainpage.AdminFragment
import com.example.trabalho.mainpage.GestorFragment
import com.example.trabalho.ui.utilizador.UtilizadorFragment

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Descobre o role
        val role = intent.getStringExtra("ROLE")
            ?: getSharedPreferences("sessao", MODE_PRIVATE).getString("ROLE", "UTILIZADOR")

        // 2) Carrega o fragment adequado
        val fragment = when (role?.uppercase()) {
            "ADMINISTRADOR"  -> AdminFragment()
            "GESTOR_PROJETO" -> GestorFragment()
            else             -> UtilizadorFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

package com.example.trabalho.ui.utilizador

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trabalho.R
import com.example.trabalho.util.ProjetosAssociadosTabFragment

class UtilizadorFragment : Fragment() {

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera o ID do utilizador gravado no login
        userId = requireContext()
            .getSharedPreferences("sessao", Context.MODE_PRIVATE)
            .getString("ID", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Layout simples com FrameLayout‑container
        return inflater.inflate(R.layout.fragment_utilizador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Injeta o fragmento que lista os projetos associados
        childFragmentManager.beginTransaction()
            .replace(
                R.id.containerUtilizador,
                ProjetosAssociadosTabFragment.newInstance(userId)
            )
            .commit()
    }
}

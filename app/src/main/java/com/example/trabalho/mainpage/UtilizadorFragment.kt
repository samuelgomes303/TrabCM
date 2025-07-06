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

    //usa o id do utilizador passado no login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = requireContext()
            .getSharedPreferences("sessao", Context.MODE_PRIVATE)
            .getString("ID", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_utilizador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //frag com os projetos associados
        childFragmentManager.beginTransaction()
            .replace(
                R.id.containerUtilizador,
                ProjetosAssociadosTabFragment.newInstance(userId)
            )
            .commit()
    }
}

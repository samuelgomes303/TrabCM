package com.example.trabalho.mainpage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trabalho.R
import com.example.trabalho.util.ProjetoGestaoTabFragment
import com.google.android.material.tabs.TabLayout


class GestorFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var gestorId: String   // ID do gestor autenticado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_gestor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lê o id do gestor das SharedPreferences
        gestorId = requireContext()
            .getSharedPreferences("sessao", Context.MODE_PRIVATE)
            .getString("ID", "") ?: ""

        tabLayout = view.findViewById(R.id.gestorTabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Projetos"))
        tabLayout.addTab(tabLayout.newTab().setText("Estatísticas"))

        // Aba por defeito
        replaceFragment(ProjetoGestaoTabFragment.newInstance("GESTOR_PROJETO", gestorId))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(
                        ProjetoGestaoTabFragment.newInstance("GESTOR_PROJETO", gestorId)
                    )
                    //1 -> replaceFragment(EstatisticasTabFragment()) // placeholder
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.containerGestor, fragment)
            .commit()
    }
}
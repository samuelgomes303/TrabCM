package com.example.trabalho.mainpage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trabalho.R
import com.example.trabalho.util.ProjetoGestaoTabFragment
import com.example.trabalho.util.UsersTabFragment
import com.google.android.material.tabs.TabLayout

class AdminFragment : Fragment() {

    private lateinit var tabLayout: TabLayout

    private val userId by lazy {
        requireContext()
            .getSharedPreferences("sessao", Context.MODE_PRIVATE)
            .getString("ID", "") ?: ""
    }

    private val role by lazy { "ADMINISTRADOR" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_admin, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.adminTabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Projetos"))
        tabLayout.addTab(tabLayout.newTab().setText("Utilizadores"))
        tabLayout.addTab(tabLayout.newTab().setText("Estatísticas"))

        // Fragmento inicial por defeito
        replaceFragment(ProjetoGestaoTabFragment.newInstance(role))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(ProjetoGestaoTabFragment.newInstance(role))
                    1 -> replaceFragment(UsersTabFragment.newInstance(role, userId))
                    // 2 -> replaceFragment(EstatisticasTabFragment())  // se/quando necessário
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("AdminDebug", "replaceFragment -> ${fragment::class.simpleName}")
        childFragmentManager.beginTransaction()
            .replace(R.id.containerProjetos, fragment)
            .commit()
    }
}

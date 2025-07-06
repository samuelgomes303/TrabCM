package com.example.trabalho.mainpage

import android.os.Bundle
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

        //entra por defeito com os projetos
        replaceFragment(ProjetoGestaoTabFragment())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(ProjetoGestaoTabFragment())
                    1 -> replaceFragment(UsersTabFragment())
                    //2 -> replaceFragment(EstatisticasTabFragment())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    //entra por defeito com o frag admin
    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.containerProjetos, fragment)
            .commit()
    }
}



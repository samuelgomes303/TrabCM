package com.example.trabalho.util

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.ViewModel.RegistoViewModelFactory
import com.example.trabalho.data.UserEntity
import com.example.trabalho.databinding.DialogEditUserBinding
import com.example.trabalho.databinding.FragmentUsersTabBinding
import com.example.trabalho.databinding.ItemUserBinding
import com.example.trabalho.repository.UserRepository
import com.example.trabalho.viewModel.RegistoViewModel
import kotlinx.coroutines.launch


class UsersTabFragment : Fragment() {

    companion object {
        private const val ARG_ROLE   = "ROLE"
        private const val ARG_USERID = "USER_ID"
        fun newInstance(role: String, userId: String) =
            UsersTabFragment().apply {
                Log.d("UsersDebug", "newInstance → role=$role")
                arguments = bundleOf(ARG_ROLE to role, ARG_USERID to userId)
            }
    }


    private var _vb: FragmentUsersTabBinding? = null
    private val vb get() = _vb!!

    private lateinit var userRepo: UserRepository

    private val registoVM: RegistoViewModel by viewModels {
        RegistoViewModelFactory(requireContext())
    }


    private val adapter = UsersAdapter()


    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        userRepo = UserRepository(BaseDados.getInstance(ctx).userDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?
    ): View {
        _vb = FragmentUsersTabBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recycler da lista de users
        vb.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter       = this@UsersTabFragment.adapter
        }

        //visível apenas para admin
        val role = requireArguments().getString(ARG_ROLE) ?: "UTILIZADOR"
        vb.fabCriarUser.visibility =
            if (role.uppercase() == "ADMINISTRADOR") View.VISIBLE else View.GONE

        //caixa de criação
        vb.fabCriarUser.setOnClickListener { mostrarDialogoNovoUser() }

        adapter.onEditClick   = { mostrarDialogoEditar(it) }
        adapter.onDeleteClick = { mostrarDialogoRemover(it) }

        // resultado do metodo de RegistoViewModel
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            registoVM.registoState.collect { st ->
                when (st) {
                    is RegistoViewModel.RegistoState.Success -> {
                        Toast.makeText(requireContext(),
                            "Utilizador criado!", Toast.LENGTH_SHORT).show()
                        refreshUsers()               // refaz a lista
                    }
                    is RegistoViewModel.RegistoState.Error ->
                        Toast.makeText(requireContext(),
                            st.message, Toast.LENGTH_LONG).show()
                    else -> Unit
                }
            }
        }

        refreshUsers()
    }


    // criaçao do novo utilizador
    private fun mostrarDialogoNovoUser() {
        val dBind = DialogEditUserBinding.inflate(layoutInflater)

        //caixa de seleçao com os role como array
        val roles = arrayOf("ADMINISTRADOR", "GESTOR_PROJETO", "UTILIZADOR")
        dBind.spinnerRole.adapter =
            ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, roles)

        AlertDialog.Builder(requireContext())
            .setTitle("Novo utilizador")
            .setView(dBind.root)
            .setPositiveButton("Guardar") { _, _ ->
                registoVM.registar(
                    nome            = dBind.editNome.text.toString().trim(),
                    email           = dBind.editEmail.text.toString().trim(),
                    senha           = dBind.editPassword.text.toString(),
                    confirmarSenha  = dBind.editPasswordConf.text.toString(),
                    fotoUri         = null
                )

                //update do role acontece apos fechar a app
                val chosenRole = dBind.spinnerRole.selectedItem.toString()
                if (chosenRole != "UTILIZADOR") {
                    registoVM.viewModelScope.launch {
                        registoVM.registoState.collect { st ->
                            if (st is RegistoViewModel.RegistoState.Success) {
                                val u = userRepo.getUserByEmail(
                                    dBind.editEmail.text.toString().trim()
                                ) ?: return@collect
                                userRepo.updateUser(u.copy(role = chosenRole))
                                refreshUsers()
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun mostrarDialogoEditar(user: UserEntity) {
        val dBind = DialogEditUserBinding.inflate(layoutInflater)

        dBind.apply {
            editNome.setText(user.nome)
            editEmail.setText(user.email)

            val roles = arrayOf("ADMINISTRADOR", "GESTOR_PROJETO", "UTILIZADOR")
            spinnerRole.adapter =
                ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, roles)
            spinnerRole.setSelection(roles.indexOf(user.role))
            // password oculta
            editPassword.visibility     = View.GONE
            editPasswordConf.visibility = View.GONE
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar utilizador")
            .setView(dBind.root)
            .setPositiveButton("Guardar") { _, _ ->
                val actualizado = user.copy(
                    nome  = dBind.editNome.text.toString().trim(),
                    email = dBind.editEmail.text.toString().trim(),
                    role  = dBind.spinnerRole.selectedItem as String
                )
                lifecycleScope.launch {
                    userRepo.updateUser(actualizado)
                    refreshUsers()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun mostrarDialogoRemover(user: UserEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remover utilizador")
            .setMessage("Tens a certeza que queres remover ${user.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                lifecycleScope.launch {
                    userRepo.deleteUser(user)
                    refreshUsers()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun refreshUsers() = viewLifecycleOwner.lifecycleScope.launch {
        adapter.submitList(userRepo.getAllUsers())
    }

    override fun onDestroyView() { super.onDestroyView(); _vb = null }


    //recycler do item_user
    private inner class UsersAdapter :
        ListAdapter<UserEntity, UsersAdapter.VH>(Diff) {

        var onEditClick:   (UserEntity) -> Unit = {}
        var onDeleteClick: (UserEntity) -> Unit = {}

        override fun onCreateViewHolder(p: ViewGroup, v: Int) =
            VH(ItemUserBinding.inflate(layoutInflater, p, false))

        override fun onBindViewHolder(h: VH, i: Int) = h.bind(getItem(i))

        inner class VH(private val b: ItemUserBinding) :
            RecyclerView.ViewHolder(b.root) {

            fun bind(u: UserEntity) = with(b) {
                textUsername.text = u.nome
                btnEdit.setOnClickListener   { onEditClick(u) }
                btnDelete.setOnClickListener { onDeleteClick(u) }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(o: UserEntity, n: UserEntity) = o.id == n.id
        override fun areContentsTheSame(o: UserEntity, n: UserEntity) = o == n
    }
}

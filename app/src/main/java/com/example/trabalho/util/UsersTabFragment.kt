package com.example.trabalho.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.data.UserEntity
import com.example.trabalho.databinding.FragmentUsersTabBinding
import com.example.trabalho.databinding.ItemUserBinding
import com.example.trabalho.repository.UserRepository
import kotlinx.coroutines.launch

class UsersTabFragment : Fragment() {

    companion object {
        private const val ARG_ROLE = "ROLE"
        fun newInstance(role: String) = UsersTabFragment().apply {
            arguments = bundleOf(ARG_ROLE to role)
        }
    }

    private var _binding: FragmentUsersTabBinding? = null
    private val binding get() = _binding!!

    private val role by lazy { arguments?.getString(ARG_ROLE) ?: "UTILIZADOR" }

    private val userManager by lazy {
            UserRepository(BaseDados.getInstance(requireContext()).userDao())
    }

    private val adapter = UsersAdapter { user ->
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerUsers.adapter = adapter

        //mostra o botão criar so para admin
        binding.fabCriarUser.visibility = if (role.uppercase() == "ADMINISTRADOR") VISIBLE else GONE

        binding.fabCriarUser.setOnClickListener {
        }

        //carregar lista de utilizadores
        lifecycleScope.launch {
            val lista = userManager.getAllUsers()
            adapter.submitList(lista)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //adapter para listar os users
    private inner class UsersAdapter(
        val onUserClick: (UserEntity) -> Unit
    ) : ListAdapter<UserEntity, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return UserViewHolder(binding)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = getItem(position)
            holder.bind(user)
        }

        inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(user: UserEntity) {
                binding.textUsername.text = user.nome
                binding.root.setOnClickListener { onUserClick(user) }
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity) = oldItem == newItem
    }
}

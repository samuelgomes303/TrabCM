package com.example.trabalho.ui.utilizador

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.trabalho.ViewModel.TaskViewModelFactory
import com.example.trabalho.databinding.FragmentUtilizadorBinding
import com.example.trabalho.viewModel.TaskViewModel
import kotlinx.coroutines.launch

class UtilizadorFragment : Fragment() {

    private var _binding: FragmentUtilizadorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(requireContext())
    }

    private val adapter = TaskAdapter { tarefa ->
        // TODO: abrir detalhe ou registar execução
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUtilizadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTarefas.adapter = adapter
        binding.recyclerTarefas.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // Obtém o ID do utilizador guardado em SharedPreferences após login
        val userId = requireContext()
            .getSharedPreferences("sessao", Context.MODE_PRIVATE)
            .getString("ID", "") ?: ""

        viewModel.carregarTarefasDoUtilizador(userId)

        // Observa as tarefas e actualiza a UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collect { lista ->
                adapter.submitList(lista)
                binding.fragmentToolbar.subtitle =
                    "Pendentes: ${lista.count { it.state != "CONCLUIDO" }}/${lista.size}"
                    }
                    }

                binding.fabAdd.setOnClickListener {
                    // TODO: abrir bottom‑sheet ou activity para registar execução
                }
            }

            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
        }

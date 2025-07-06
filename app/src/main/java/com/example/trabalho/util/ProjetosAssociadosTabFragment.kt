package com.example.trabalho.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.data.ProjectEntity
import com.example.trabalho.databinding.FragmentProjetosAssociadosTabBinding
import com.example.trabalho.databinding.ItemProjectBinding
import com.example.trabalho.repository.TaskIndicationRepository
import kotlinx.coroutines.launch

class ProjetosAssociadosTabFragment : Fragment() {


    companion object {
        private const val ARG_USER_ID = "USER_ID"
        fun newInstance(userId: String) =
            ProjetosAssociadosTabFragment().apply {
                arguments = bundleOf(ARG_USER_ID to userId)
            }
    }


    private var _binding: FragmentProjetosAssociadosTabBinding? = null
    private val binding get() = _binding!!


    private val userId by lazy { requireArguments().getString(ARG_USER_ID) ?: "" }

    private val taskIndicationRepo by lazy {
        TaskIndicationRepository(
            BaseDados.getInstance(requireContext()).taskIndicationDao()
        )
    }


    private val adapter = ProjectsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjetosAssociadosTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerProjetos.adapter = adapter

        //lista os projetos passados pelo repository
        lifecycleScope.launch {
            val projetos = taskIndicationRepo.getProjectsByUser(userId)
            adapter.submitList(projetos)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // adapter para a lista de projetos e tarefas
    private inner class ProjectsAdapter : ListAdapter<ProjectEntity, ProjectsAdapter.ProjectVH>(ProjectDiff()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectVH {
            val itemBinding = ItemProjectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ProjectVH(itemBinding)
        }

        override fun onBindViewHolder(holder: ProjectVH, position: Int) {
            holder.bind(getItem(position))
        }

        inner class ProjectVH(private val b: ItemProjectBinding) :
            RecyclerView.ViewHolder(b.root) {

            fun bind(p: ProjectEntity) = with(b) {
                txtNomeProjeto.text = p.nome
                txtEstado.text = when (p.state) {
                    "CONCLUIDO"     -> "Concluído"
                    "EM_PROGRESSO"  -> "Em curso"
                    "CANCELADO"     -> "Cancelado"
                    else            -> "Desconhecido"
                }

                root.setOnClickListener {
                    // logica dentro da tarefa(por fazer)
                }
            }
        }
    }

    private class ProjectDiff : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(old: ProjectEntity, new: ProjectEntity) = old.id == new.id
        override fun areContentsTheSame(old: ProjectEntity, new: ProjectEntity) = old == new
    }
}

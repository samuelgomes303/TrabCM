package com.example.trabalho.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.data.ProjectEntity
import com.example.trabalho.databinding.FragmentProjetoGestaoTabBinding
import com.example.trabalho.databinding.ItemProjectBinding
import com.example.trabalho.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjetoGestaoTabFragment : Fragment() {
    companion object {
        private const val ARG_ROLE = "ROLE"
        private const val ARG_USER_ID = "USER_ID" // ID do gestor autenticado
        fun newInstance(role: String, userId: String) =
            ProjetoGestaoTabFragment().apply {
                arguments = bundleOf(ARG_ROLE to role, ARG_USER_ID to userId)
            }
    }

    private var _binding: FragmentProjetoGestaoTabBinding? = null
    private val binding get() = _binding!!

    private val role by lazy { arguments?.getString(ARG_ROLE) ?: "UTILIZADOR" }
    private val gestorId by lazy { arguments?.getString(ARG_USER_ID) ?: "" }

    private val projectRepo by lazy {
        ProjectRepository(BaseDados.getInstance(requireContext()).projectDao())
    }

    private var selectedProject: ProjectEntity? = null

    private val adapter = ProjectsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjetoGestaoTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerProjetos.adapter = adapter
        updateActionButtonsVisibility()

        binding.btnAssociarUsuarios.setOnClickListener {
            selectedProject?.let { project ->
                openAssociarUsuariosDialog(project)
            } ?: showMessage("Selecione um projeto")
        }

        binding.btnCriarTarefa.setOnClickListener {
            selectedProject?.let { project ->
                openCriarTarefaDialog(project)
            } ?: showMessage("Selecione um projeto")
        }

        binding.fabNovoProjeto.setOnClickListener {
            // TODO: abrir tela para criar novo projeto
        }

        // Carregar projetos do gestor autenticado
        lifecycleScope.launch {
            val projetos = projectRepo.getProjectsByManager(gestorId)
            adapter.submitList(projetos)
        }
    }

    private fun updateActionButtonsVisibility() {
        val visible = role.uppercase() == "GESTOR_PROJETO" && selectedProject != null
        binding.layoutBottomButtons.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun openAssociarUsuariosDialog(project: ProjectEntity) {
        // TODO: abrir dialog para selecionar e associar usuários
        showMessage("Associar usuários ao projeto: ${project.nome}")
    }

    private fun openCriarTarefaDialog(project: ProjectEntity) {
        // TODO: abrir dialog para criar tarefa associada a usuário do projeto
        showMessage("Criar tarefa no projeto: ${project.nome}")
    }


    private inner class ProjectsAdapter :
        ListAdapter<ProjectEntity, ProjectsAdapter.ProjectVH>(ProjectDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectVH {
            val row = ItemProjectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ProjectVH(row)
        }

        override fun onBindViewHolder(holder: ProjectVH, position: Int) =
            holder.bind(getItem(position))

        inner class ProjectVH(private val b: ItemProjectBinding) :
            RecyclerView.ViewHolder(b.root) {

            fun bind(p: ProjectEntity) = with(b) {
                txtNomeProjeto.text = p.nome
                txtGestorNome.text  = p.idGestor    // ou buscar nome via UserRepo
                txtEstado.text      = when (p.state) {
                    "CONCLUIDO"    -> "Concluído"
                    "EM_PROGRESSO" -> "Em Progresso"
                    "CANCELADO"    -> "Cancelado"
                    else           -> "Desconhecido"
                }

                // Realce visual
                val sel = (p == selectedProject)
                itemView.setBackgroundColor(
                    if (sel) resources.getColor(android.R.color.holo_blue_light, null)
                    else     resources.getColor(android.R.color.transparent, null)
                )

                itemView.setOnClickListener {
                    selectedProject = if (sel) null else p   // desfaz se clicar no mesmo
                    updateActionButtonsVisibility()
                    this@ProjectsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private object ProjectDiffCallback : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(o: ProjectEntity, n: ProjectEntity) = o.id == n.id
        override fun areContentsTheSame(o: ProjectEntity, n: ProjectEntity) = o == n
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

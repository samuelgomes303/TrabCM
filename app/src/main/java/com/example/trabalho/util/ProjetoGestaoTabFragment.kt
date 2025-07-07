package com.example.trabalho.util

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.MOD.BaseDados
import com.example.trabalho.R
import com.example.trabalho.data.ProjectEntity
import com.example.trabalho.databinding.DialogEditProjectBinding
import com.example.trabalho.databinding.FragmentProjetoTabBinding
import com.example.trabalho.repository.ProjectRepository
import com.example.trabalho.repository.UserRepository
import kotlinx.coroutines.launch

class ProjetoGestaoTabFragment : Fragment() {

    companion object {
        private const val ARG_ROLE = "ROLE"
        fun newInstance(role: String) = ProjetoGestaoTabFragment().apply {
            arguments = bundleOf(ARG_ROLE to role)
        }
    }


    private var _b: FragmentProjetoTabBinding? = null
    private val b get() = _b!!

    //repositorios
    private lateinit var projectRepo: ProjectRepository
    private lateinit var userRepo: UserRepository

    //adapter
    private val adapter = ProjectsAdapter()


    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        val db = BaseDados.getInstance(ctx)
        projectRepo = ProjectRepository(db.projectDao())
        userRepo    = UserRepository(db.userDao())
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentProjetoTabBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        // recycler de projetos
        b.recyclerProjects.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerProjects.adapter = adapter

        // só para ADMIN
        val role = requireArguments().getString(ARG_ROLE) ?: "UTILIZADOR"
        b.fabCriarProjeto.visibility =
            if (role.uppercase() == "ADMINISTRADOR") View.VISIBLE else View.GONE

        b.fabCriarProjeto.setOnClickListener { abrirDialogoCriarProjeto() }

        //remove ou edita e atualiza
        adapter.onEditClick   = { abrirDialogoEditarProjeto(it) }
        adapter.onDeleteClick = { proj ->
            AlertDialog.Builder(requireContext())
                .setTitle("Remover projeto")
                .setMessage("Remover \"${proj.nome}\"?")
                .setPositiveButton("Sim") { _, _ ->
                    lifecycleScope.launch {
                        projectRepo.deleteProject(proj)
                        refreshProjects()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        refreshProjects()
    }

    //input para criar projeto
    private fun abrirDialogoCriarProjeto() {
        val dBind = DialogEditProjectBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            val gestores = userRepo.getAllUsers().filter { it.role == "GESTOR_PROJETO" }
            val nomes    = gestores.map { it.nome }
            val ids      = gestores.map { it.id }

            dBind.spinnerGestor.adapter =
                ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, nomes)

            AlertDialog.Builder(requireContext())
                .setTitle("Novo Projeto")
                .setView(dBind.root)
                .setPositiveButton("Guardar") { _, _ ->
                    val nome = dBind.editNome.text.toString().trim()
                    if (nome.isBlank()) return@setPositiveButton

                    val desc   = dBind.editDescription.text.toString().trim()
                    val idxSel = dBind.spinnerGestor.selectedItemPosition
                    val idGest = ids.getOrElse(idxSel) { "" }

                    lifecycleScope.launch {
                        projectRepo.createNewProject(nome, desc, idGest)
                        refreshProjects()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun abrirDialogoEditarProjeto(p: ProjectEntity) {
        val dBind = DialogEditProjectBinding.inflate(layoutInflater)
        dBind.editNome.setText(p.nome)
        dBind.editDescription.setText(p.description)

        lifecycleScope.launch {
            val gestores = userRepo.getAllUsers().filter { it.role == "GESTOR_PROJETO" }
            val nomes    = gestores.map { it.nome }
            val ids      = gestores.map { it.id }

            dBind.spinnerGestor.adapter =
                ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, nomes)

            dBind.spinnerGestor.setSelection(ids.indexOf(p.idGestor).coerceAtLeast(0))

            AlertDialog.Builder(requireContext())
                .setTitle("Editar Projeto")
                .setView(dBind.root)
                .setPositiveButton("Guardar") { _, _ ->
                    val novo = p.copy(
                        nome        = dBind.editNome.text.toString().trim(),
                        description = dBind.editDescription.text.toString().trim(),
                        idGestor    = ids.getOrElse(
                            dBind.spinnerGestor.selectedItemPosition
                        ) { p.idGestor }
                    )
                    lifecycleScope.launch {
                        projectRepo.updateProject(novo)
                        refreshProjects()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }


    private fun refreshProjects() = lifecycleScope.launch {
        adapter.submitList(projectRepo.getAllProjects())
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }


    private inner class ProjectsAdapter :
        ListAdapter<ProjectEntity, ProjectsAdapter.VH>(Diff) {

        var onEditClick:   (ProjectEntity) -> Unit = {}
        var onDeleteClick: (ProjectEntity) -> Unit = {}

        override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
            val row = LayoutInflater.from(p.context)
                .inflate(R.layout.item_project, p, false)
            return VH(row)
        }

        override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            private val txt = v.findViewById<TextView>(R.id.textProjectName)
            private val btnE = v.findViewById<ImageButton>(R.id.btnEdit)
            private val btnD = v.findViewById<ImageButton>(R.id.btnDelete)

            fun bind(p: ProjectEntity) {
                txt.text = p.nome
                btnE.setOnClickListener { onEditClick(p) }
                btnD.setOnClickListener { onDeleteClick(p) }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ProjectEntity>() {
        override fun areItemsTheSame(o: ProjectEntity, n: ProjectEntity) = o.id == n.id
        override fun areContentsTheSame(o: ProjectEntity, n: ProjectEntity) = o == n
    }
}

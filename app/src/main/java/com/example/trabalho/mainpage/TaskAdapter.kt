package com.example.trabalho.ui.utilizador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho.data.TaskEntity
import com.example.trabalho.databinding.ItemSimplesTarefaBinding

class TaskAdapter(private val onClick: (TaskEntity) -> Unit) : ListAdapter<TaskEntity, TaskAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemSimplesTarefaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemSimplesTarefaBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(t: TaskEntity) = with(b) {
            txtTitulo.text = t.title
            txtEstado.text = when (t.state) {
                "A_FAZER"     -> "Por fazer"
                "EM_PROGRESSO"-> "Em progresso"
                else          -> "Concluída"
            }
            root.setOnClickListener { onClick(t) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<TaskEntity>() {
            override fun areItemsTheSame(o: TaskEntity, n: TaskEntity) = o.id == n.id
            override fun areContentsTheSame(o: TaskEntity, n: TaskEntity) = o == n
        }
    }
}

package org.example.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.Task

class TaskAdapter(
    private val onTaskClicked: (Task) -> Unit,
    private val onTaskCheckedChanged: (Task, Boolean) -> Unit,
    private val onDeleteClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var items: List<Task> = emptyList()

    // PUBLIC_INTERFACE
    fun submitList(newItems: List<Task>) {
        // Simple update (no DiffUtil to keep dependencies minimal)
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(v)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = items.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkCompleted: CheckBox = itemView.findViewById(R.id.checkTaskCompleted)
        private val txtTitle: TextView = itemView.findViewById(R.id.txtTaskTitle)
        private val txtNotes: TextView = itemView.findViewById(R.id.txtTaskNotes)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(task: Task) {
            // Prevent old listeners from firing when re-binding
            checkCompleted.setOnCheckedChangeListener(null)

            txtTitle.text = task.title
            txtNotes.text = task.notes
            txtNotes.visibility = if (task.notes.isBlank()) View.GONE else View.VISIBLE

            checkCompleted.isChecked = task.completed
            checkCompleted.setOnCheckedChangeListener { _, isChecked ->
                onTaskCheckedChanged(task, isChecked)
            }

            itemView.setOnClickListener { onTaskClicked(task) }
            btnDelete.setOnClickListener { onDeleteClicked(task) }
        }
    }
}

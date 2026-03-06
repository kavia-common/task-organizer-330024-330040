package org.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.example.app.data.TaskDatabaseHelper
import org.example.app.data.TaskRepository
import org.example.app.ui.TaskAdapter
import org.example.app.ui.addedit.AddEditTaskActivity
import org.example.app.viewmodel.TaskListViewModel
import org.example.app.viewmodel.TaskListViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TaskListViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = TaskDatabaseHelper(applicationContext)
        val repository = TaskRepository(dbHelper)
        val factory = TaskListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskListViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(
            onTaskClicked = { task ->
                val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                    putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.id)
                }
                startActivity(intent)
            },
            onTaskCheckedChanged = { task, isChecked ->
                viewModel.setCompleted(task.id, isChecked)
            },
            onDeleteClicked = { task ->
                viewModel.deleteTask(task.id)
                Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter

        // Observe DB-backed list and refresh UI
        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure list reflects any changes done in Add/Edit screen
        viewModel.refresh()
    }
}

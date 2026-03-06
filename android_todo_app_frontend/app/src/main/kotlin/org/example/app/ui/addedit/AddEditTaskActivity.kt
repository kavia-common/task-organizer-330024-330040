package org.example.app.ui.addedit

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import org.example.app.R
import org.example.app.data.Task
import org.example.app.data.TaskDatabaseHelper
import org.example.app.data.TaskRepository

class AddEditTaskActivity : ComponentActivity() {

    private lateinit var repository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        repository = TaskRepository(TaskDatabaseHelper(applicationContext))

        val editTitle = findViewById<EditText>(R.id.editTaskTitle)
        val editNotes = findViewById<EditText>(R.id.editTaskNotes)
        val checkCompleted = findViewById<CheckBox>(R.id.checkCompleted)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        val existingTask: Task? = if (taskId > 0) repository.getTaskById(taskId) else null

        if (existingTask != null) {
            title = getString(R.string.edit_task_title)
            editTitle.setText(existingTask.title)
            editNotes.setText(existingTask.notes)
            checkCompleted.isChecked = existingTask.completed
        } else {
            title = getString(R.string.add_task_title)
        }

        btnSave.setOnClickListener {
            val titleText = editTitle.text?.toString()?.trim().orEmpty()
            val notesText = editNotes.text?.toString()?.trim().orEmpty()

            if (titleText.isBlank()) {
                Toast.makeText(this, R.string.validation_title_required, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (existingTask == null) {
                repository.insertTask(
                    title = titleText,
                    notes = notesText
                )
                Toast.makeText(this, R.string.task_added, Toast.LENGTH_SHORT).show()
            } else {
                repository.updateTask(
                    taskId = existingTask.id,
                    title = titleText,
                    notes = notesText,
                    completed = checkCompleted.isChecked
                )
                Toast.makeText(this, R.string.task_updated, Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        btnCancel.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}

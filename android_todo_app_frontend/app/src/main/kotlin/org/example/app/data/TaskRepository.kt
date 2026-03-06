package org.example.app.data

import android.content.ContentValues
import android.database.Cursor

class TaskRepository(
    private val dbHelper: TaskDatabaseHelper
) {

    // PUBLIC_INTERFACE
    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_TASKS,
            arrayOf(
                TaskDatabaseHelper.COL_ID,
                TaskDatabaseHelper.COL_TITLE,
                TaskDatabaseHelper.COL_NOTES,
                TaskDatabaseHelper.COL_COMPLETED,
                TaskDatabaseHelper.COL_CREATED_AT
            ),
            null,
            null,
            null,
            null,
            "${TaskDatabaseHelper.COL_COMPLETED} ASC, ${TaskDatabaseHelper.COL_CREATED_AT} DESC"
        )

        cursor.use {
            val tasks = mutableListOf<Task>()
            while (it.moveToNext()) {
                tasks.add(cursorToTask(it))
            }
            return tasks
        }
    }

    // PUBLIC_INTERFACE
    fun getTaskById(taskId: Long): Task? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_TASKS,
            arrayOf(
                TaskDatabaseHelper.COL_ID,
                TaskDatabaseHelper.COL_TITLE,
                TaskDatabaseHelper.COL_NOTES,
                TaskDatabaseHelper.COL_COMPLETED,
                TaskDatabaseHelper.COL_CREATED_AT
            ),
            "${TaskDatabaseHelper.COL_ID}=?",
            arrayOf(taskId.toString()),
            null,
            null,
            null,
            "1"
        )

        cursor.use {
            return if (it.moveToFirst()) cursorToTask(it) else null
        }
    }

    // PUBLIC_INTERFACE
    fun insertTask(title: String, notes: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COL_TITLE, title)
            put(TaskDatabaseHelper.COL_NOTES, notes)
            put(TaskDatabaseHelper.COL_COMPLETED, 0)
            put(TaskDatabaseHelper.COL_CREATED_AT, System.currentTimeMillis())
        }
        return db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values)
    }

    // PUBLIC_INTERFACE
    fun updateTask(taskId: Long, title: String, notes: String, completed: Boolean): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COL_TITLE, title)
            put(TaskDatabaseHelper.COL_NOTES, notes)
            put(TaskDatabaseHelper.COL_COMPLETED, if (completed) 1 else 0)
        }
        return db.update(
            TaskDatabaseHelper.TABLE_TASKS,
            values,
            "${TaskDatabaseHelper.COL_ID}=?",
            arrayOf(taskId.toString())
        )
    }

    // PUBLIC_INTERFACE
    fun deleteTask(taskId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            TaskDatabaseHelper.TABLE_TASKS,
            "${TaskDatabaseHelper.COL_ID}=?",
            arrayOf(taskId.toString())
        )
    }

    // PUBLIC_INTERFACE
    fun setTaskCompleted(taskId: Long, completed: Boolean): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TaskDatabaseHelper.COL_COMPLETED, if (completed) 1 else 0)
        }
        return db.update(
            TaskDatabaseHelper.TABLE_TASKS,
            values,
            "${TaskDatabaseHelper.COL_ID}=?",
            arrayOf(taskId.toString())
        )
    }

    private fun cursorToTask(cursor: Cursor): Task {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COL_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COL_TITLE))
        val notes = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COL_NOTES))
        val completedInt = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COL_COMPLETED))
        val createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COL_CREATED_AT))
        return Task(
            id = id,
            title = title,
            notes = notes,
            completed = completedInt == 1,
            createdAtEpochMs = createdAt
        )
    }
}

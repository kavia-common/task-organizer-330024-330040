package org.example.app.data

data class Task(
    val id: Long,
    val title: String,
    val notes: String,
    val completed: Boolean,
    val createdAtEpochMs: Long
)

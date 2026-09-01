package com.example.todoapp.data.repository

import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.model.TodoUpdatePayload
import com.example.todoapp.data.model.TodoUpdateRequest
import com.example.todoapp.data.remote.TodoApi

class TodoRepository(
    private val todoApi: TodoApi
) {

    suspend fun getTodos(): List<Todo> {
        return todoApi
            .getTodos()
            .data
    }

    suspend fun updateTodo(
        documentId: String,
        title: String,
        description: String?,
        completed: Boolean,
        priority: String
    ): Todo {
        val request = TodoUpdateRequest(
            data = TodoUpdatePayload(
                title = title,
                description = description,
                completed = completed,
                priority = priority
            )
        )
        return todoApi
            .updateTodo(documentId, request)
            .data
    }
}
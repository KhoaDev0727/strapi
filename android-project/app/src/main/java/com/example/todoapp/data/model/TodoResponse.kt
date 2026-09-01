package com.example.todoapp.data.model

data class TodoResponse(
    val data: List<Todo>,
    val meta: Meta
)

data class TodoSingleResponse(
    val data: Todo
)

data class TodoUpdateRequest(
    val data: TodoUpdatePayload
)

data class TodoUpdatePayload(
    val title: String,
    val description: String?,
    val completed: Boolean,
    val priority: String
)

data class Meta(
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val pageSize: Int,
    val pageCount: Int,
    val total: Int
)
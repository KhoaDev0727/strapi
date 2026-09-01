package com.example.todoapp.data.remote

import com.example.todoapp.data.model.TodoResponse
import com.example.todoapp.data.model.TodoSingleResponse
import com.example.todoapp.data.model.TodoUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApi {

    @GET("api/todos")
    suspend fun getTodos(): TodoResponse

    @PUT("api/todos/{documentId}")
    suspend fun updateTodo(
        @Path("documentId") documentId: String,
        @Body request: TodoUpdateRequest
    ): TodoSingleResponse
}
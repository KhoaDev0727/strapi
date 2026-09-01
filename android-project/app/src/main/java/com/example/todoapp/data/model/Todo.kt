package com.example.todoapp.data.model

import com.google.gson.annotations.SerializedName

data class Todo(
    val id: Int,

    @SerializedName("documentId")
    val documentId: String,

    val title: String,

    val description: String?,

    val completed: Boolean,

    val priority: String
)
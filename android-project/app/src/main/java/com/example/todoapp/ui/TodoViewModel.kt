package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.remote.RetrofitInstance
import com.example.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TodoUiState {

    data object Loading : TodoUiState

    data class Success(
        val todos: List<Todo>
    ) : TodoUiState

    data class Error(
        val message: String
    ) : TodoUiState
}

class TodoViewModel : ViewModel() {

    private val repository = TodoRepository(
        RetrofitInstance.api
    )

    private val _uiState =
        MutableStateFlow<TodoUiState>(
            TodoUiState.Loading
        )

    val uiState: StateFlow<TodoUiState> =
        _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {

        viewModelScope.launch {

            _uiState.value = TodoUiState.Loading

            try {

                val todos = repository.getTodos()

                _uiState.value =
                    TodoUiState.Success(todos)

            } catch (e: Exception) {

                _uiState.value =
                    TodoUiState.Error(
                        e.message ?: "Unknown error"
                    )
            }
        }
    }

    fun updateTodo(
        documentId: String,
        title: String,
        description: String?,
        completed: Boolean,
        priority: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updatedTodo = repository.updateTodo(
                    documentId = documentId,
                    title = title,
                    description = description,
                    completed = completed,
                    priority = priority
                )

                val currentState = _uiState.value
                if (currentState is TodoUiState.Success) {
                    val updatedList = currentState.todos.map { todo ->
                        if (todo.documentId == documentId) updatedTodo else todo
                    }
                    _uiState.value = TodoUiState.Success(updatedList)
                }

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update todo")
            }
        }
    }

    fun getTodoByDocumentId(
        documentId: String?
    ): Todo? {

        if (documentId == null) {
            return null
        }

        val currentState = _uiState.value

        return if (currentState is TodoUiState.Success) {

            currentState.todos.find { todo ->
                todo.documentId == documentId
            }

        } else {
            null
        }
    }
}
package com.example.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo
import com.example.todoapp.ui.components.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    onTodoClick: (Todo) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(

        topBar = {
            TodoTopBar()
        }

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (val state = uiState) {

                TodoUiState.Loading -> {

                    LoadingContent()
                }

                is TodoUiState.Success -> {

                    TodoList(
                        todos = state.todos,
                        onTodoClick = onTodoClick
                    )
                }

                is TodoUiState.Error -> {

                    ErrorContent(
                        message = state.message
                    )
                }
            }
        }
    }
}


/**
 * Top App Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoTopBar() {

    CenterAlignedTopAppBar(

        title = {

            Text(
                text = "My Tasks"
            )
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}


/**
 * Todo List
 */
@Composable
private fun TodoList(
    todos: List<Todo>,
    onTodoClick: (Todo) -> Unit
) {

    /**
     * Nếu chưa có Todo
     */
    if (todos.isEmpty()) {

        EmptyTodoContent()

        return
    }

    /**
     * Danh sách Todo
     */
    LazyColumn(

        modifier = Modifier
            .fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),

        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {

        items(

            items = todos,

            key = { todo ->
                todo.documentId
            }

        ) { todo ->

            TodoItem(

                todo = todo,

                onClick = {
                    onTodoClick(todo)
                }
            )
        }
    }
}


/**
 * Empty state
 *
 * Hiển thị khi Strapi trả về []
 */
@Composable
private fun EmptyTodoContent() {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Text(

            text = "No tasks",

            style = MaterialTheme.typography.headlineSmall
        )

        Text(

            text = "No tasks available",

            style = MaterialTheme.typography.bodyMedium,

            color = MaterialTheme.colorScheme.onSurfaceVariant,

            modifier = Modifier.padding(
                top = 8.dp
            )
        )
    }
}


/**
 * Loading state
 *
 * Hiển thị trong lúc Android
 * đang gọi API Strapi.
 */
@Composable
private fun LoadingContent() {

    Column(

        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        CircularProgressIndicator()

        Text(

            text = "Loading tasks...",

            style = MaterialTheme.typography.bodyMedium,

            modifier = Modifier.padding(
                top = 16.dp
            )
        )
    }
}


/**
 * Error state
 *
 * Hiển thị khi API Strapi
 * trả về lỗi hoặc network bị lỗi.
 */
@Composable
private fun ErrorContent(
    message: String
) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Text(

            text = "Something went wrong",

            style = MaterialTheme.typography.headlineSmall
        )

        Text(

            text = message,

            style = MaterialTheme.typography.bodyMedium,

            color = MaterialTheme.colorScheme.error,

            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}
package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.TodoScreen
import com.example.todoapp.ui.TodoViewModel
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.ui.navigation.TodoNavigation
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            TodoAppTheme {

                val todoViewModel: TodoViewModel = viewModel()

                TodoNavigation(
                    viewModel = todoViewModel
                )
            }
        }
    }
}
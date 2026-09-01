package com.example.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.ui.TodoDetailScreen
import com.example.todoapp.ui.TodoScreen
import com.example.todoapp.ui.TodoViewModel

@Composable
fun TodoNavigation(
    viewModel: TodoViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todos"
    ) {

        composable(
            route = "todos"
        ) {

            TodoScreen(
                viewModel = viewModel,

                onTodoClick = { todo ->

                    navController.navigate(
                        "todo/${todo.documentId}"
                    )
                }
            )
        }

        composable(
            route = "todo/{documentId}",

            arguments = listOf(
                navArgument("documentId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val documentId =
                backStackEntry.arguments?.getString("documentId")

            val todo = viewModel.getTodoByDocumentId(
                documentId = documentId
            )

            if (todo != null) {

                TodoDetailScreen(
                    todo = todo,
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
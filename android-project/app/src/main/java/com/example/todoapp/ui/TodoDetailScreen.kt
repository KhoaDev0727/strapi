package com.example.todoapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo
import com.example.todoapp.ui.components.StatusIndicator
import com.example.todoapp.ui.theme.PriorityHighBg
import com.example.todoapp.ui.theme.PriorityHighBorder
import com.example.todoapp.ui.theme.PriorityHighText
import com.example.todoapp.ui.theme.PriorityLowBg
import com.example.todoapp.ui.theme.PriorityLowBorder
import com.example.todoapp.ui.theme.PriorityLowText
import com.example.todoapp.ui.theme.PriorityMediumBg
import com.example.todoapp.ui.theme.PriorityMediumBorder
import com.example.todoapp.ui.theme.PriorityMediumText
import com.example.todoapp.ui.theme.StatusCompletedText
import com.example.todoapp.ui.theme.StatusPendingText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todo: Todo,
    viewModel: TodoViewModel,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf(todo.title) }
    var description by remember { mutableStateOf(todo.description ?: "") }
    var completed by remember { mutableStateOf(todo.completed) }
    var priority by remember { mutableStateOf(todo.priority.lowercase().ifBlank { "medium" }) }
    var isSaving by remember { mutableStateOf(false) }
    var isTitleError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val priorities = listOf("low", "medium", "high")

    fun onSave() {
        if (title.isBlank()) {
            isTitleError = true
            scope.launch {
                snackbarHostState.showSnackbar("Title cannot be empty")
            }
            return
        }

        isSaving = true
        viewModel.updateTodo(
            documentId = todo.documentId,
            title = title.trim(),
            description = description.trim().ifEmpty { null },
            completed = completed,
            priority = priority,
            onSuccess = {
                isSaving = false
                onBackClick()
            },
            onError = { errorMessage ->
                isSaving = false
                scope.launch {
                    snackbarHostState.showSnackbar("Failed to update: $errorMessage")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Task",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        enabled = !isSaving
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onSave() },
                        enabled = !isSaving && title.isNotBlank()
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Save",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (isTitleError && it.isNotBlank()) {
                        isTitleError = false
                    }
                },
                label = { Text("Title") },
                placeholder = { Text("Enter task title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = isTitleError,
                supportingText = if (isTitleError) {
                    { Text("Title is required") }
                } else null,
                singleLine = true,
                enabled = !isSaving
            )

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("Enter task description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 4,
                maxLines = 8,
                enabled = !isSaving
            )

            // Priority Selection with vibrant colors
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    priorities.forEach { p ->
                        val isSelected = priority.equals(p, ignoreCase = true)
                        val (chipBg, chipText, chipBorder) = when (p) {
                            "low" -> Triple(PriorityLowBg, PriorityLowText, PriorityLowBorder)
                            "medium" -> Triple(PriorityMediumBg, PriorityMediumText, PriorityMediumBorder)
                            "high" -> Triple(PriorityHighBg, PriorityHighText, PriorityHighBorder)
                            else -> Triple(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                Color.Transparent
                            )
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (!isSaving) {
                                    priority = p
                                }
                            },
                            label = {
                                Text(
                                    text = p.replaceFirstChar { it.uppercase() },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = chipBg,
                                selectedLabelColor = chipText,
                                selectedLeadingIconColor = chipText
                            ),
                            border = if (isSelected) {
                                BorderStroke(1.5.dp, chipBorder)
                            } else {
                                FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = false
                                )
                            },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else null,
                            enabled = !isSaving
                        )
                    }
                }
            }

            // Status Card with Tick/Cross Indicator
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatusIndicator(completed = completed, size = 34)

                        Column {
                            Text(
                                text = "Status",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (completed) "Completed" else "Not Completed",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = if (completed) StatusCompletedText else StatusPendingText
                            )
                        }
                    }

                    Switch(
                        checked = completed,
                        onCheckedChange = {
                            if (!isSaving) {
                                completed = it
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = StatusCompletedText,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = StatusPendingText.copy(alpha = 0.5f)
                        ),
                        enabled = !isSaving
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = { onSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSaving && title.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
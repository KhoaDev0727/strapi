package com.example.todoapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo
import com.example.todoapp.ui.theme.PriorityHighBg
import com.example.todoapp.ui.theme.PriorityHighBorder
import com.example.todoapp.ui.theme.PriorityHighText
import com.example.todoapp.ui.theme.PriorityLowBg
import com.example.todoapp.ui.theme.PriorityLowBorder
import com.example.todoapp.ui.theme.PriorityLowText
import com.example.todoapp.ui.theme.PriorityMediumBg
import com.example.todoapp.ui.theme.PriorityMediumBorder
import com.example.todoapp.ui.theme.PriorityMediumText
import com.example.todoapp.ui.theme.StatusCompletedBg
import com.example.todoapp.ui.theme.StatusCompletedBorder
import com.example.todoapp.ui.theme.StatusCompletedText
import com.example.todoapp.ui.theme.StatusPendingBg
import com.example.todoapp.ui.theme.StatusPendingBorder
import com.example.todoapp.ui.theme.StatusPendingText

@Composable
fun TodoItem(
    todo: Todo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.5.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Indicator (Tick ✓ for Completed, Cross ✕ for Pending)
            StatusIndicator(completed = todo.completed)

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                        textDecoration = if (todo.completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                        color = if (todo.completed) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    PriorityBadge(
                        priority = todo.priority
                    )
                }

                todo.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = if (todo.completed) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

/**
 * Status Indicator with tick ✓ for completed, cross ✕ for pending
 */
@Composable
fun StatusIndicator(
    completed: Boolean,
    modifier: Modifier = Modifier,
    size: Int = 30
) {
    val bgColor = if (completed) StatusCompletedBg else StatusPendingBg
    val borderColor = if (completed) StatusCompletedBorder else StatusPendingBorder
    val iconColor = if (completed) StatusCompletedText else StatusPendingText

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(BorderStroke(1.5.dp, borderColor), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (completed) Icons.Filled.Check else Icons.Filled.Close,
            contentDescription = if (completed) "Completed" else "Not Completed",
            tint = iconColor,
            modifier = Modifier.size((size * 0.55).dp)
        )
    }
}

/**
 * Priority badge with color based on priority level:
 * - Low: Green
 * - Medium: Orange
 * - High: Red
 */
@Composable
fun PriorityBadge(
    priority: String?,
    modifier: Modifier = Modifier
) {
    if (priority.isNullOrBlank()) {
        return
    }

    val (bgColor, textColor, borderColor) = when (priority.lowercase().trim()) {
        "low" -> Triple(PriorityLowBg, PriorityLowText, PriorityLowBorder)
        "medium" -> Triple(PriorityMediumBg, PriorityMediumText, PriorityMediumBorder)
        "high" -> Triple(PriorityHighBg, PriorityHighText, PriorityHighBorder)
        else -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            Color.Transparent
        )
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Text(
            text = priority.uppercase().trim(),
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 3.dp
            ),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )
    }
}
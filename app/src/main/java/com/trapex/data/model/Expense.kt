package com.trapex.data.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val company: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val icon: String = "💰",
    val color: Color = Color(0xFF6200EA)
) {
    val formattedAmount: String
        get() = "$${String.format("%.2f", amount)}"
    
    val timeAgo: String
        get() {
            val now = LocalDateTime.now()
            val diff = java.time.Duration.between(timestamp, now)
            return when {
                diff.toMinutes() < 60 -> "${diff.toMinutes()}m ago"
                diff.toHours() < 24 -> "${diff.toHours()}h ago"
                diff.toDays() < 30 -> "${diff.toDays()}d ago"
                else -> "${diff.toDays() / 30}mo ago"
            }
        }
}

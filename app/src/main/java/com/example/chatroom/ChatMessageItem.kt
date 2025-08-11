package com.example.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatMessageItem(message: Message) {
    val alignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isSentByCurrentUser) Color(0xFF6200EE) else Color.Gray,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.White, fontSize = 16.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(text = message.senderFirstName, fontSize = 12.sp, color = Color.DarkGray)
        Text(text = formatTimestamp(message.timestamp), fontSize = 12.sp, color = Color.DarkGray)
    }
}

/** Simple formatter that works on all API levels */
fun formatTimestamp(timestamp: Long): String {
    val now = Calendar.getInstance()
    val msgCal = Calendar.getInstance().apply { timeInMillis = timestamp }

    val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFmt = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    val sameDay = now.get(Calendar.YEAR) == msgCal.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR)

    val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterdayCal.get(Calendar.YEAR) == msgCal.get(Calendar.YEAR)
            && yesterdayCal.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR)

    return when {
        sameDay -> "today ${timeFmt.format(Date(timestamp))}"
        isYesterday -> "yesterday ${timeFmt.format(Date(timestamp))}"
        else -> dateFmt.format(Date(timestamp))
    }
}

package ai.chat.x.ai.images.presentation.components

import ai.chat.x.ai.images.data.model.Message
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserChatCard(content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray,
            ),
        ) {
            Text(
                text = content,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun AssistantChatCard(content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White,
            ),
        ) {
            Text(
                text = content,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ChatListItem(message: Message) {
    when (message.role) {
        "user" -> {
            UserChatCard(message.content)
        }
        "assistant" -> {
            AssistantChatCard(message.content)
        }
        else -> {
            DefaultChatCard(message.content)
        }
    }
}

@Composable
fun DefaultChatCard(content: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = content,
            modifier = Modifier.padding(16.dp)
        )
    }
}



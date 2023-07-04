package ai.chat.x.ai.images.presentation

import ai.chat.x.ai.images.data.model.Message
import ai.chat.x.ai.images.utils.CustomLogger
import ai.chat.x.ai.images.presentation.components.ChatBox
import ai.chat.x.ai.images.presentation.components.ChatListItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ai.chat.x.ai.images.ui.theme.AIChatXAIImagesTheme
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    var chatGptApiKey: String = ""

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val ai: ApplicationInfo = applicationContext.packageManager
                .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            val value = ai.metaData["chatGptApiKey"]

            chatGptApiKey = value.toString()

            val chatList = remember { mutableStateListOf<Message>() }

            CustomLogger.i("Test info")

            AIChatXAIImagesTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chatList) { item ->
                            ChatListItem(item)
                        }
                    }

                    ChatBox(chatGptApiKey, chatList = chatList)
                }
            }
        }
    }
}

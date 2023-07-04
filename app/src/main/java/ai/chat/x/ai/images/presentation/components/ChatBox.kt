package ai.chat.x.ai.images.presentation.components

import ai.chat.x.ai.images.data.model.Message
import ai.chat.x.ai.images.data.repository.getAIResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(chatGptApiKey: String, chatList: MutableList<Message>) {
    var myVar by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
                .align(Alignment.BottomCenter)
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Max)
        ) {
            TextField(

                value = myVar,
                onValueChange = { newText ->
                    myVar = newText
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                visualTransformation = VisualTransformation.None
            )
            Button(
                onClick = {
                    val newMessage = Message(role = "user", content = myVar.text)
                    chatList.add(newMessage)

                    getAIResponse(chatGptApiKey, chatList)

                    myVar = TextFieldValue("")
                }
            ) {
                Text(text = "Send")
            }
        }
    }
}
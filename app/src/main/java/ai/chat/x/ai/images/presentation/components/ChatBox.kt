package ai.chat.x.ai.images.presentation.components

import ai.chat.x.ai.images.R
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.data.model.ImageChatItem
import ai.chat.x.ai.images.data.model.LoaderChatItem
import ai.chat.x.ai.images.data.model.MessageChatItem
import ai.chat.x.ai.images.data.repository.getAiText
import ai.chat.x.ai.images.domain.AiImageHandler
import ai.chat.x.ai.images.domain.ChatListHandler
import ai.chat.x.ai.images.utils.Logger
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatBox(
    applicationContext: Context,
    chatGptApiKey: String,
    chatItemList: MutableList<ChatItem>,
    chatBoxTextFieldValue: MutableState<TextFieldValue>,
) {
    var isTextMode by remember { mutableStateOf(true) }

    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .align(Alignment.BottomCenter)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = {
                    isTextMode = !isTextMode
                },
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(if (isTextMode) R.drawable.message_square_svgrepo_com else R.drawable.image_1_svgrepo_com),
                    contentDescription = if (isTextMode) "Text Mode" else "ImageChatItem Mode"
                )
            }

            TextField(
                value = chatBoxTextFieldValue.value,
                onValueChange = { newText ->
                    chatBoxTextFieldValue.value = newText
                    Logger.d("TextField value changed")
                },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                visualTransformation = VisualTransformation.None,
            )

            IconButton(
                onClick = {
                    handleSendClick(applicationContext, chatGptApiKey, chatItemList, chatBoxTextFieldValue.value.text, isTextMode, softwareKeyboardController)
                    chatBoxTextFieldValue.value = TextFieldValue("")
                },
                enabled = chatBoxTextFieldValue.value.text.isNotEmpty(),
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.send_fill0_wght400_grad0_opsz48),
                    contentDescription = "Send"
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleSendClick(
    applicationContext: Context,
    chatGptApiKey: String,
    chatItemList: MutableList<ChatItem>,
    userRequestText: String,
    isTextMode: Boolean,
    softwareKeyboardController: SoftwareKeyboardController?
) {
    softwareKeyboardController?.hide()

    if (isTextMode) {
        chatItemList.add(MessageChatItem(role = "user", content = userRequestText))

        // TODO: Create text handler.
        getAiText(
            chatGptApiKey,
            ChatListHandler.getChatItemListJsonString(chatItemList),
            onSuccess = { messageJsonString ->
                chatItemList.removeLast()
                chatItemList.add(Gson().fromJson(messageJsonString, MessageChatItem::class.java))
            },
            onError = { errorMessage ->
                // TODO: Handle request error, retry snackbar?
                chatItemList.removeLast()
            }
        )

        // Add loader
        chatItemList.add(LoaderChatItem(role = "loader", content = ""));
    } else {
        chatItemList.add(ImageChatItem(role = "user", content = userRequestText, imagePath = ""))

        AiImageHandler.getAIImage(
            applicationContext,
            chatItemList,
            onSuccess  = { imagePath ->
                chatItemList.removeLast()
                chatItemList.add(ImageChatItem(role = "assistant", content = userRequestText, imagePath = imagePath))
            },
            onError = { errorMessage ->
                // TODO: Handle request error, retry snackbar?
                chatItemList.removeLast()
            }
        )

        // Add loader
        chatItemList.add(LoaderChatItem(role = "loader", content = ""));
    }
}

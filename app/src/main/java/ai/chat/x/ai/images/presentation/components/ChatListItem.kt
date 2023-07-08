package ai.chat.x.ai.images.presentation.components

import ai.chat.x.ai.images.R
import ai.chat.x.ai.images.data.local.ClipboardHandler
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.data.model.ImageChatItem
import ai.chat.x.ai.images.data.model.LoaderChatItem
import ai.chat.x.ai.images.data.model.MessageChatItem
import ai.chat.x.ai.images.domain.StringHelper
import ai.chat.x.ai.images.utils.Logger
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

val CardEndMargin = 64.dp
val CardPadding = 16.dp

@Composable
fun ChatListItem(
    chatItem: ChatItem,
    chatBoxTextFieldValue: MutableState<TextFieldValue>,
    replyChatItem: MutableState<ChatItem?>
) {
    when (chatItem) {
        is MessageChatItem -> {
            when (chatItem.role) {
                "user" -> {
                    UserMessageChatItemView(chatItem, chatBoxTextFieldValue, replyChatItem)
                }
                "assistant" -> {
                    AssistantMessageChatItemView(chatItem, chatBoxTextFieldValue, replyChatItem)
                }
            }
        }
        is ImageChatItem -> {
            when (chatItem.role) {
                "user" -> {
                    UserMessageChatItemView(chatItem, chatBoxTextFieldValue, replyChatItem)
                }
                "assistant" -> {
                    AssistantImageChatItemView(chatItem)
                }
            }
        }

        is LoaderChatItem -> {
            LoaderChatItemItemView(chatItem)
        }
    }
}

/*
 * Message ChatItem.
 */
@Composable
fun UserMessageChatItemView(
    chatItem: ChatItem,
    chatBoxTextFieldValue: MutableState<TextFieldValue>,
    replyChatItem: MutableState<ChatItem?>
) {
    val context = LocalContext.current
    var expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier
                .padding(CardEndMargin, 4.dp, CardPadding, 4.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = chatItem.content,
                    modifier = Modifier.padding(8.dp)
                )

                IconButton(
                    onClick = { expanded.value = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Copy") },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.copy_svgrepo_com), contentDescription = "Copy")},
                            onClick = {
                                if(ClipboardHandler.copyTextToClipboard(context, chatItem.content))
                                    Toast.makeText(
                                        context,
                                        "Copied: ${StringHelper.getTruncatedString(chatItem.content, 20)}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                expanded.value = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Edit") },

                            onClick = {
                                // TODO: Move keyboard cursor to end of pasted text.
                                chatBoxTextFieldValue.value = TextFieldValue(chatItem.content)

                                expanded.value = false
                            },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.edit_4_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "share") },
                            onClick = { /*TODO: Share message as text */ },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.share_2_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "reply") },
                            onClick = {
                                replyChatItem.value = chatItem
                                expanded.value = false
                            },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.reply_svgrepo_com), contentDescription = "Copy")}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AssistantMessageChatItemView(
    chatItem: MessageChatItem,
    chatBoxTextFieldValue: MutableState<TextFieldValue>,
    replyChatItem: MutableState<ChatItem?>
) {
    val context = LocalContext.current
    var expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier.padding(8.dp, 4.dp, CardEndMargin, 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = chatItem.content,
                    modifier = Modifier.padding(8.dp)
                )

                IconButton(
                    onClick = { expanded.value = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Copy") },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.copy_svgrepo_com), contentDescription = "Copy")},
                            onClick = {
                                if(ClipboardHandler.copyTextToClipboard(context, chatItem.content))
                                    Toast.makeText(
                                        context,
                                        "Copied: ${StringHelper.getTruncatedString(chatItem.content, 20)}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                expanded.value = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Edit") },

                            onClick = {
                                // TODO: Move keyboard cursor to end of pasted text.
                                chatBoxTextFieldValue.value = TextFieldValue(chatItem.content)

                                expanded.value = false
                            },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.edit_4_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "share") },
                            onClick = { /*TODO: Share message as text */ },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.share_2_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "reply") },
                            onClick = {
                                replyChatItem.value = chatItem
                                expanded.value = false
                            },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.reply_svgrepo_com), contentDescription = "Copy")}
                        )
                    }
                }
            }
        }
    }
}

/*
 * Image ChatItem
 */
@Composable
fun AssistantImageChatItemView(imageChatItem: ImageChatItem) {
    val context = LocalContext.current
    var expanded = remember { mutableStateOf(false) }

    Logger.d(imageChatItem.content)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp, 8.dp, CardEndMargin, 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = File(imageChatItem.imagePath),
                    contentDescription = imageChatItem.content,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.aspectRatio(1f)
                )

                IconButton(
                    onClick = { expanded.value = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Copy") },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.copy_svgrepo_com), contentDescription = "Copy")},
                            onClick = {
                                if(ClipboardHandler.copyTextToClipboard(context, imageChatItem.content))
                                    Toast.makeText(
                                        context,
                                        "Copied: " + imageChatItem.content.substring(0, 20),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                expanded.value = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Edit") },

                            onClick = {
                                expanded.value = false
                            },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.edit_4_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "share") },
                            onClick = { /*TODO: Share image */ },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.share_2_svgrepo_com), contentDescription = "Copy")}
                        )
                        DropdownMenuItem(
                            text = { Text(text = "reply") },
                            onClick = { /*TODO: Edit image*/ },
                            leadingIcon = {Icon(imageVector = ImageVector.vectorResource(R.drawable.reply_svgrepo_com), contentDescription = "Copy")}
                        )
                    }
                }
            }

//            Text(
//                text = imageChatItem.content,
//                modifier = Modifier.padding(8.dp)
//            )
        }
    }
}

@Composable
fun LoaderChatItemItemView(chatItem: LoaderChatItem){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
        Text(text = chatItem.content)
    }
}

/*
 * Replies ChatItems
 */
@Composable
fun UserMessageChatItemReplyView(
    replyChatItemMutableState: MutableState<ChatItem?>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = replyChatItemMutableState.value?.content ?: "",
                    modifier = Modifier.padding(8.dp)
                )

                IconButton(
                    onClick = { replyChatItemMutableState.value = null },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Options")
                }
            }
        }
    }
}

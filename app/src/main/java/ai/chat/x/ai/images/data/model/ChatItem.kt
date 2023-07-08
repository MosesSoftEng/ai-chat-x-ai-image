package ai.chat.x.ai.images.data.model

sealed interface ChatItem {
    val role: String
    val content: String
}

// TODO: Rename message to text.
data class MessageChatItem(
    override val role: String,
    override val content: String,
) : ChatItem

data class ImageChatItem(
    override val role: String,
    override val content: String,
    val imagePath: String
) : ChatItem

data class LoaderChatItem(
    override val role: String,
    override val content: String,
) : ChatItem

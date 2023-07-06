package ai.chat.x.ai.images.data.model

sealed interface ChatItem {
    val role: String
    val content: String
}

data class Message(
    override val role: String,
    override val content: String,
) : ChatItem

data class Image(
    override val role: String,
    override val content: String,
    val imagePath: String
) : ChatItem

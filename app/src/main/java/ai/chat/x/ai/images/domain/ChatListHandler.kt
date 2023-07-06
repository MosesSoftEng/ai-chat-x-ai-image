package ai.chat.x.ai.images.domain

import ai.chat.x.ai.images.data.model.ChatItem

object ChatListHandler {
    fun getLatestMessage(chatItemList: List<ChatItem>): ChatItem? {
        return chatItemList.lastOrNull()
    }
}
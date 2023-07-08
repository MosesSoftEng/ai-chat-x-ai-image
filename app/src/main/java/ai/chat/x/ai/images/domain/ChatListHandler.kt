package ai.chat.x.ai.images.domain

import ai.chat.x.ai.images.data.model.ChatItem
import com.google.gson.Gson

object ChatListHandler {
    fun getLatestMessage(chatItemList: List<ChatItem>): ChatItem? {
        return chatItemList.lastOrNull()
    }

    fun getChatItemListJsonString(chatItemList: List<ChatItem>): String {
        return Gson().toJson(chatItemList)
    }
}
package ai.chat.x.ai.images.domain

import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.data.model.LoaderChatItem
import com.google.gson.Gson

object ChatListHandler {
    fun getLatestMessage(chatItemList: List<ChatItem>): ChatItem? {
        return chatItemList.lastOrNull()
    }

    fun getChatItemListJsonString(chatItemList: List<ChatItem>): String {
        return Gson().toJson(chatItemList)
    }

    fun showLoader(chatItemList: MutableList<ChatItem>) {
        chatItemList.add(LoaderChatItem(role = "loader", content = ""));
    }
}
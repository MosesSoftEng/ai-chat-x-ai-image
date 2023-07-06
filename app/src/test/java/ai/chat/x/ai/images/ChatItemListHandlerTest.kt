package ai.chat.x.ai.images

import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.domain.ChatListHandler
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChatItemListHandlerTest {
    @Test
    fun `test getLatestMessage with non-empty chatList`() {
        // Arrange
        val chatItemLists = listOf(
            ChatItem("user", "Hello"),
            ChatItem("assistant", "Hi"),
            ChatItem("user", "How are you?")
        )

        // Act
        val latestMessage = ChatListHandler.getLatestMessage(chatItemLists)

        // Assert
        assertEquals(ChatItem("user", "How are you?"), latestMessage)
    }

    @Test
    fun `test getLatestMessage with empty chatList`() {
        // Arrange
        val chatItemList = emptyList<ChatItem>()

        // Act
        val latestMessage = ChatListHandler.getLatestMessage(chatItemList)

        // Assert
        assertEquals(null, latestMessage)
    }
}

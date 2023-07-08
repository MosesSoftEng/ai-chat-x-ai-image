package ai.chat.x.ai.images

import ai.chat.x.ai.images.data.model.MessageChatItem
import ai.chat.x.ai.images.domain.ChatListHandler
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChatItemListHandlerUnitTest {
    @Test
    fun `test getLatestMessage with non-empty chatList`() {
        // Arrange
        val chatItemLists = listOf(
            MessageChatItem("user", "Hello"),
            MessageChatItem("assistant", "Hi"),
            MessageChatItem("user", "How are you?")
        )

        // Act
        val latestMessage = ChatListHandler.getLatestMessage(chatItemLists)

        // Assert
        assertEquals(MessageChatItem("user", "How are you?"), latestMessage)
    }

    @Test
    fun `test getLatestMessage with empty chatList`() {
        // Arrange
        val chatItemList = emptyList<MessageChatItem>()

        // Act
        val latestMessage = ChatListHandler.getLatestMessage(chatItemList)

        // Assert
        assertEquals(null, latestMessage)
    }
}

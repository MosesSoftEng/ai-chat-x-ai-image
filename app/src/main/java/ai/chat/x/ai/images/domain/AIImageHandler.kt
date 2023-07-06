package ai.chat.x.ai.images.domain

import ai.chat.x.ai.images.config.KEYS
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.data.repository.AiImageRepo
import ai.chat.x.ai.images.utils.Logger
import org.json.JSONArray

// TODO: Rename classname to AiImageHandler.
object AIImageHandler {
    fun getAIImage(chatItemList: MutableList<ChatItem>) {
        val lastestMessage: ChatItem = ChatListHandler.getLatestMessage(chatItemList) ?: return

        AiImageRepo.makeAiImageRequest(
            KEYS.OPENAI_API,
            lastestMessage.content,
            1,
            "256x256",
            onSuccess  = { imagesDataList  ->
                processAiImages(imagesDataList );
            },
            onError = { response ->
                Logger.d(response)
            }
        )

        // TODO: Handle fail situation.

        // TODO: Add ai image response to chatItemList.


        // TODO: Save image locally.
    }

    private fun processAiImages(imagesDataList: JSONArray) {
        for (i in 0 until imagesDataList.length()) {
            val imageObject = imagesDataList.getJSONObject(i)
            val imageUrl = imageObject.optString("url")

            Logger.d(imageUrl)
        }
    }
}

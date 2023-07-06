package ai.chat.x.ai.images.data.repository

import ai.chat.x.ai.images.data.model.Message
import ai.chat.x.ai.images.utils.CustomLogger
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

val url = "https://api.openai.com/v1/chat/completions"

fun getAITextResponse(chatGptApiKey: String, chatList: MutableList<Message>) {
    val client = OkHttpClient()

    val gson = Gson()
    val chatListJson = gson.toJson(chatList)

    CustomLogger.i(chatListJson)

    val json = """
        {
            "model": "gpt-3.5-turbo",
            "messages": $chatListJson
        }
    """.trimIndent()

    val mediaType = "application/json".toMediaType()
    val request = Request.Builder()
        .url(url)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer $chatGptApiKey")
        .post(json.toRequestBody(mediaType))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Handle API call failure
            println(e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()

            CustomLogger.i(responseBody.toString())

            if (response.isSuccessful) {
                // Process the response here
                // responseBody contains the response from the API
                val jsonResponse = JSONObject(responseBody)
                val choicesArray = jsonResponse.getJSONArray("choices")


                if (choicesArray.length() > 0) {
                    val firstChoice = choicesArray.getJSONObject(0)
                    val messageJsonString = firstChoice.getString("message").trim()

                    val newMessage = gson.fromJson(messageJsonString, Message::class.java)
                    chatList.add(newMessage)
                }
            } else {
                // Handle API call failure
                println("failed")
            }
        }
    })
}

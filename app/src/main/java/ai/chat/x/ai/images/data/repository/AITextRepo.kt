package ai.chat.x.ai.images.data.repository

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


// TODO: Rename to getAiTextResponse
fun getAiText(
    chatGptApiKey: String,
    chatListJson: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()

    val gson = Gson()

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
            e.message?.let { onError(it) }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(responseBody)
                val choicesArray = jsonResponse.getJSONArray("choices")

                if (choicesArray.length() > 0) {
                    val firstChoice = choicesArray.getJSONObject(0)
                    val messageJsonString = firstChoice.getString("message").trim()

                    onSuccess(messageJsonString)
                }
            } else {
                // TODO: Handle error
                error("Failed to get ai text response")
            }
        }
    })
}

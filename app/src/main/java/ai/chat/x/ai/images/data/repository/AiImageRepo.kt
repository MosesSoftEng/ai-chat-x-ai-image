package ai.chat.x.ai.images.data.repository

import ai.chat.x.ai.images.utils.Logger
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

// TODO: Rename to AiImageAPI
object AiImageRepo {
    fun makeAiImageRequest(
        apiKey: String,
        prompt: String,
        n: Int,
        size: String,
        onSuccess: (JSONArray) -> Unit,
        onError: (String) -> Unit)  {

        Logger.d("Get image from open ai")

        val url = "https://api.openai.com/v1/images/generations"
        val client = OkHttpClient()
        val requestBody = """
            {
                "prompt": "$prompt",
                "n": $n,
                "size": "$size"
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object: okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error occurred")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                Logger.d(requestBody)

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val imagesDataList = jsonResponse.optJSONArray("data")

                    if (imagesDataList != null) {
                        onSuccess(imagesDataList)
                    } else {
                        onError("Invalid response format")
                    }
                } else {
                    onError("Request failed")
                }
            }
        })
    }
}


package ai.chat.x.ai.images

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ai.chat.x.ai.images.ui.theme.AIChatXAIImagesTheme
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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


class MainActivity : ComponentActivity() {
    var chatGptApiKey: String = ""

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val ai: ApplicationInfo = applicationContext.packageManager
                .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            val value = ai.metaData["chatGptApiKey"]

            chatGptApiKey = value.toString()

            val chatList = remember { mutableStateListOf<Message>() }


            CustomLogger.i("Test info")

            AIChatXAIImagesTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chatList) { item ->
                            ChatListItem(item.content)
                        }
                    }
                    ChatBox(chatList = chatList)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChatBox(chatList: MutableList<Message>) {
        var myVar by remember { mutableStateOf(TextFieldValue("")) }

        Box() {
            Row(
                modifier = Modifier
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter)
                    .height(IntrinsicSize.Min)
            ) {
                TextField(
                    value = myVar,
                    onValueChange = { newText ->
                        myVar = newText
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    visualTransformation = VisualTransformation.None
                )
                Button(
                    onClick = {
                        val newMessage = Message(role = "user", content = myVar.text)
                        chatList.add(newMessage)

                        getAIResponse(myVar.text, chatGptApiKey, chatList)

                        myVar = TextFieldValue("")
                    }
                ) {
                    Text(text = "Send")
                }
            }
        }
    }

    fun getAIResponse(prompt: String, chatGptApiKey: String, chatList: MutableList<Message>) {
        val client = OkHttpClient()
        val url = "https://api.openai.com/v1/completions"

        val gson = Gson()
        val chatListJson = gson.toJson(chatList)

        CustomLogger.i(chatListJson)

        val json = """
        {
            "model": "gpt-3.5-turbo",
            "messages":  [{"role": "system", "content": "You are a helpful assistant."}, {"role": "user", "content": "Hello!"}]
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
                        val text = firstChoice.getString("text").trim()

                        val newMessage = Message(role = "assistant", content = text)
                        chatList.add(newMessage)
                    }
                } else {
                    // Handle API call failure
                    println("failed")
                }
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChatListItem(message: String) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

data class Message(
    val role: String,
    val content: String
)

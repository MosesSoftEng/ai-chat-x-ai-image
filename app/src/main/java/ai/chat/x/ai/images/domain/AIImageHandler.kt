package ai.chat.x.ai.images.domain

import ai.chat.x.ai.images.config.KEYS
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.data.repository.AiImageRepo
import ai.chat.x.ai.images.utils.Logger
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename classname to AiImageHandler.
object AiImageHandler {
    fun getAIImage(
        applicationContext: Context,
        chatItemList: MutableList<ChatItem>,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )
    {
        val latestMessage: ChatItem = ChatListHandler.getLatestMessage(chatItemList) ?: return

        AiImageRepo.makeAiImageRequest(
            KEYS.OPENAI_API,
            latestMessage.content,
            1,
            "256x256",
            onSuccess = { imagesDataList  ->
                processAiImages(
                    applicationContext,
                    imagesDataList,
                    latestMessage.content,
                    onSuccess  = { imagePath ->
                        onSuccess(imagePath)
                    },
                    onError = { response ->
                        Logger.d(response)
                    }
                );
            },
            onError = { response ->
                Logger.d(response)
            }
        )

        // TODO: Handle fail situation.

        // TODO: Add ai image response to chatItemList.


        // TODO: Save image locally.
    }

    private fun processAiImages(
        applicationContext: Context,
        imagesDataList: JSONArray,
        imageDescription: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val expectedCount = imagesDataList.length()
        var successCount = 0

        for (i in 0 until imagesDataList.length()) {
            val imageObject = imagesDataList.getJSONObject(i)
            val imageUrl = imageObject.optString("url")

            Logger.d(imageUrl)

            saveImageFromUrl(
                applicationContext,
                imageUrl,
                generateImageFilename(imageDescription, i),
                onSuccess = {imagePath ->
                    successCount++

                    if (successCount == expectedCount) {
                        onSuccess.invoke(imagePath)
                    }
                }
            )
        }
    }


    private fun saveImageFromUrl(
        context: Context,
        imageUrl: String,
        imageName: String,
        onSuccess: (String) -> Unit
    ) {
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap != null) {
                    val picturesDirectory =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val imageFile = File(picturesDirectory, imageName)

                    try {
                        val outputStream = FileOutputStream(imageFile)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                        Toast.makeText(context, "ImageChatItem saved successfully", Toast.LENGTH_SHORT)
                            .show()
                        onSuccess.invoke(imageFile.path.toString())
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // Do nothing
            }
        }

        Handler(Looper.getMainLooper()).post {
            Picasso.get()
                .load(imageUrl)
                .into(target)
        }
    }

    private fun generateImageFilename(description: String, variation: Int): String {
        val cleanDescription = description
            .replace("[^a-zA-Z0-9\\s-]".toRegex(), "")
            .replace("\\s+".toRegex(), "-")
            .trim()
            .toLowerCase(Locale.ROOT)

        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT).format(Date())

        val limitedDescription = cleanDescription.take(50)

        return "$limitedDescription-$variation-$timestamp.jpg"
    }
}

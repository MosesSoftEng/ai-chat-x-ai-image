package ai.chat.x.ai.images.presentation.main

import ai.chat.x.ai.images.common.PermissionManager
import ai.chat.x.ai.images.config.KEYS
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.presentation.components.CenteredCircularProgressIndicator
import ai.chat.x.ai.images.presentation.components.ChatBox
import ai.chat.x.ai.images.presentation.components.ChatListItem
import ai.chat.x.ai.images.ui.theme.AIChatXAIImagesTheme
import ai.chat.x.ai.images.utils.Logger
import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    private var chatGptApiKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // TODO: Create a service to get key.
            val ai: ApplicationInfo = applicationContext.packageManager
                .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            val value = ai.metaData["chatGptApiKey"]

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                storagePermission()

            // TODO: Delete this line.
            chatGptApiKey = value.toString()
            KEYS.OPENAI_API = value.toString()

            val chatItemList = remember { mutableStateListOf<ChatItem>() }
            var chatBoxTextFieldValue = remember { mutableStateOf(TextFieldValue("")) }

            AIChatXAIImagesTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chatItemList) { item ->
                            ChatListItem(item, chatBoxTextFieldValue)
                        }
                    }

                    ChatBox(applicationContext, chatGptApiKey, chatItemList = chatItemList, chatBoxTextFieldValue)
                }
            }
        }
    }

    // TODO: Move below functions to PermissionManager.
    private fun storagePermission() {
        if (!PermissionManager.isStoragePermissionGranted(this)) {
            Logger.d("Get storage permission")

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                /* TODO: Show a dialog to explain the need for storage permission
                   with a button that calls PermissionManager.requestStoragePermission(this, PermissionManager.STORAGE_PERMISSION_REQUEST_CODE) method. */
                Logger.d("Explain the need for storage permission")

                Toast.makeText(
                    this,
                    "Storage permission is required to save AI images.",
                    Toast.LENGTH_SHORT
                ).show()

                PermissionManager.requestStoragePermission(this, PermissionManager.STORAGE_PERMISSION_REQUEST_CODE)
            } else {
                PermissionManager.requestStoragePermission(this, PermissionManager.STORAGE_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Logger.d("Storage permission granted")
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    /* TODO: Show a dialog asking the user to manually grant app storage permission
                       that calls the method openAppSettings() */
                    Logger.d("Permission permanently denied")

                    openAppSettings()
                } else {
                    /* TODO: Show a dialog to let the user know that the app will not function correctly without
                       storage permission */
                    Logger.d("Storage permission denied")
                    // Storage permission denied
                    // Handle accordingly (e.g., show an error message, disable certain features)
                }
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}

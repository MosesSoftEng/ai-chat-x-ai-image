package ai.chat.x.ai.images.presentation

import ai.chat.x.ai.images.common.PermissionManager
import ai.chat.x.ai.images.common.PermissionManager.requestStoragePermission
import ai.chat.x.ai.images.config.KEYS
import ai.chat.x.ai.images.data.model.ChatItem
import ai.chat.x.ai.images.utils.Logger
import ai.chat.x.ai.images.presentation.components.ChatBox
import ai.chat.x.ai.images.presentation.components.ChatListItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ai.chat.x.ai.images.ui.theme.AIChatXAIImagesTheme
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    var chatGptApiKey: String = ""

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val ai: ApplicationInfo = applicationContext.packageManager
                .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            val value = ai.metaData["chatGptApiKey"]

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // For Android 10 and above, request permission using the new approach
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//                startActivityForResult(intent, 1000)
//
//                // Proceed with your image read/write operations
//                // ...
//                Logger.d("Proceed with your image read/write operations")
//            } else {
//                val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
//                val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
//                val permissions = arrayOf(readPermission, writePermission)
//                val requestCode = 1001 // You can use any integer value here
//
//                // For Android versions prior to Android 10, request permission using the legacy approach
//                ActivityCompat.requestPermissions(this, permissions, requestCode)
//            }

            // TODO: Delete this line.
            chatGptApiKey = value.toString()
            KEYS.OPENAI_API = value.toString()

            val chatItemList = remember { mutableStateListOf<ChatItem>() }

            Logger.d("Test info")

            AIChatXAIImagesTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chatItemList) { item ->
                            ChatListItem(item)
                        }
                    }

                    ChatBox(chatGptApiKey, chatItemList = chatItemList)
                }
            }
        }
    }

    // Handle the permission request result in onRequestPermissionsResult() callback method
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) { // Check if this is the same request code used above
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your image read/write operations
                // ...
                Logger.d("Permission granted")
            } else {
                // Permission denied, handle accordingly (e.g., show an error message)
                // ...
                Logger.d("Permission denied")

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = data?.data
                if (uri != null) {
                    val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)

                    // Proceed with your image read/write operations
                    // ...
                    Logger.d("Permission granted")
                } else {
                    // Handle the case where the URI is null
                    Logger.d("Permission denied")
                }
            }
        }
    }


//    private fun getStoragePermissionAndroid13() {
//        // Check if we have the permission to read and write to external storage
//        val permissions = arrayOf(
//            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.READ_MEDIA_AUDIO
//        )
//        val permissionStatus = permissions.map { ContextCompat.checkSelfPermission(this, it) }
//
//        if (permissionStatus.any { it != PackageManager.PERMISSION_GRANTED }) {
//            // We don't have the permission, so request
//            Logger.d("We don't have the permission, so request it")
//            requestPermissions(permissions, 100)
//        } else {
//            // We already have the permission, so proceed
//            // Do something that requires the permission
//            Logger.d("We already have the permission, so proceed")
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//            // The permission was granted, so proceed
//            // Do something that requires the permission
//            Logger.d("The permission was granted")
//        } else {
//            // The permission was denied, so show a notification
//            // or do something else
//            Logger.d("The permission was denied")
//        }
//    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Check if permissions are granted
            val readGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            val writeGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

            if (readGranted && writeGranted) {
                // Permissions granted, proceed with your image read/write operations
                // ...
                Logger.d("Proceed with your image read/write operations")
            } else {
                // Permissions denied, handle accordingly (e.g., show an error message)
                // ...
                Logger.d("Permissions denied, handle accordingly")
            }
        }

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        requestPermissionLauncher.launch(permissions)
    }
}
package ai.chat.x.ai.images.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    private val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun isStoragePermissionGranted(context: Context): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            storagePermission
        )
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    fun requestStoragePermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(storagePermission),
            requestCode
        )
    }
}
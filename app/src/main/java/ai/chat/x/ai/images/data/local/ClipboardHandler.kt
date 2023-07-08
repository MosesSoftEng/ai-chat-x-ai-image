package ai.chat.x.ai.images.data.local

import ai.chat.x.ai.images.utils.Logger
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


object ClipboardHandler {
    /**
     * Copies the specified text to the clipboard.
     *
     * @param context The context.
     * @param text The text to be copied to the clipboard.
     * @return `true` if the text was successfully added to the clipboard, `false` otherwise.
     */
    fun copyTextToClipboard(context: Context, text: String): Boolean {
        Logger.d(text)

        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboardManager.setPrimaryClip(clipData)

        return clipboardManager.hasPrimaryClip()
    }
}

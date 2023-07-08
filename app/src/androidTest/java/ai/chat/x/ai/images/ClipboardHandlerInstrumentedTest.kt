package ai.chat.x.ai.images

import ai.chat.x.ai.images.data.local.ClipboardHandler
import android.content.ClipboardManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClipboardHandlerInstrumentedTest {
    @Test
    fun testCopyTextToClipboard() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val text = "Hello, World!"
        val result = ClipboardHandler.copyTextToClipboard(context, text)

        // Assert that the text was successfully copied to the clipboard
        assertEquals(true, result)
        assertEquals(text, clipboardManager.primaryClip?.getItemAt(0)?.text?.toString())
    }
}
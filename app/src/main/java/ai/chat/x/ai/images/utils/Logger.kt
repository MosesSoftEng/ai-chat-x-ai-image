package ai.chat.x.ai.images.utils

import ai.chat.x.ai.images.config.APP_NAME_ID
import ai.chat.x.ai.images.config.SHOW_LOGS
import android.util.Log

object Logger {
    fun d(vararg messages: Any) {
        if (SHOW_LOGS) {
            val stackTrace = Thread.currentThread().stackTrace
            val caller = stackTrace[3]
            val methodName = caller.methodName
            val className = caller.className
            val logMessage = "[$className.$methodName] "

            val finalMessage = StringBuilder()

            for (message in messages) {
                finalMessage.append(" ").append(message.toString())
            }

            Log.d("$APP_NAME_ID.debug", logMessage + finalMessage.toString())
        }
    }
}

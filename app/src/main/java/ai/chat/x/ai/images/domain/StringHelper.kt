package ai.chat.x.ai.images.domain

object StringHelper {
    fun getTruncatedString(input: String, maxLength: Int): String {
        return if (input.length > maxLength) {
            input.substring(0, maxLength)
        } else {
            input
        }
    }
}
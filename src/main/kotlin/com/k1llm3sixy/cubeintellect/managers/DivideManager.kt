package com.k1llm3sixy.cubeintellect.managers

import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.sendMessage
import net.minecraft.text.Text

const val MAX_MESSAGE_LENGTH = 200
object DivideManager {
    fun divideMessage(answer: String?) {
        if (!answer.isNullOrEmpty()) {
            if (answer.length <= MAX_MESSAGE_LENGTH) {
                sendMessage(Text.translatable("message.answer", answer))
            } else {
                val parts = answer.chunked(MAX_MESSAGE_LENGTH)
                parts.forEachIndexed { index, part -> sendMessage(Text.translatable("message.answer_part", index, part)) }
            }
        } else {
            sendMessage(Text.translatable("message.answer_error"))
        }
    }
}
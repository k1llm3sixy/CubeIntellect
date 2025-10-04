package com.k1llm3sixy.cubeintellect.managers

import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.sendMessage
import com.k1llm3sixy.cubeintellect.https.ApiClient
import net.minecraft.text.Text

object GeminiManager {
    fun handleQuestion(question: String) {
        if (CONFIG.apiKey.isEmpty()) {
            sendMessage(Text.translatable("message.apikey_empty"))
            return
        }

        val answer = ApiClient.fetchJson(question)
        DivideManager.divideMessage(answer)
    }
}
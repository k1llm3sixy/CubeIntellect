package com.k1llm3sixy.cubeintellect.ai

import com.google.gson.Gson
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class ApiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)
data class Content(val parts: List<Part>)
data class Part(val text: String)

object Parser {
    var key = ""
    var model = "gemini-2.0-flash"

    fun parse(question: String, source: FabricClientCommandSource) {
        try {
            Thread {
                if (key.isNotEmpty()) {
                    val connection = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent").openConnection() as HttpsURLConnection
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("X-goog-api-key", key)
                    connection.doOutput = true

                    val json = """{"contents":[{"parts":[{"text":"$question"}]}]}""".trimIndent()
                    connection.outputStream.bufferedWriter(Charsets.UTF_8).use {
                        it.write(json)
                    }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        connection.inputStream.bufferedReader(Charsets.UTF_8).use {
                            val gson = Gson()
                            val answer = gson.fromJson(
                                it,
                                ApiResponse::class.java
                            ).candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Error"
                            source.sendFeedback(Text.literal("§6» §eGeminiModel: $model"))
                            source.sendFeedback(Text.literal("§6» §eAnswer: §a$answer"))
                        }
                    } else {
                        source.sendFeedback(Text.literal("§6» Connection Error. Response code - §c$responseCode"))
                    }
                } else {
                    source.sendFeedback(Text.literal("§6» §eNo API KEY! §cuse §b/cubeintellect setKey <key> §6«"))
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
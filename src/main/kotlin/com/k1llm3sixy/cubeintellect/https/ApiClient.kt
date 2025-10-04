package com.k1llm3sixy.cubeintellect.https

import com.google.gson.Gson
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.logger
import net.minecraft.text.Text
import java.net.URI
import javax.net.ssl.HttpsURLConnection

data class Part(val text: String)
data class Content(val parts: List<Part>)
data class Candidate(val content: Content)
data class Response(val candidates: List<Candidate>)

object ApiClient {
    fun fetchJson(question: String): String? {
        try {
            val json = """{"contents":[{"parts":[{"text":"$question"}]}],"generationConfig":{"temperature":${CONFIG.temperature},"maxOutputTokens":${CONFIG.maxTokens}}}"""
            val connection = URI("https://generativelanguage.googleapis.com/v1beta/models/${CONFIG.geminiModel.modelId}:generateContent")
                .toURL()
                .openConnection() as HttpsURLConnection
            connection.setRequestProperty("x-goog-api-key", CONFIG.apiKey)
            connection.setRequestProperty("Content-Type", "application/json")
            connection.requestMethod = "POST"
            connection.doOutput = true

            connection.outputStream.use {
                val input = json.toByteArray()
                it.write(input)
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val gson = Gson()
                connection.inputStream.bufferedReader().use {
                    val response = gson.fromJson(it.readText(), Response::class.java)
                    return response.candidates
                        .firstOrNull()
                        ?.content
                        ?.parts
                        ?.firstOrNull()
                        ?.text
                }
            } else {
                logger.error("Something went wrong! Code $responseCode")
                return Text.translatable("message.answer_error").string
            }
        } catch (e: Exception) {
            logger.error("Something went wrong!", e)
            return Text.translatable("message.answer_error").string
        }
    }
}
package com.k1llm3sixy.cubeintellect.config

data class Config(
    var geminiModel: GeminiModels = GeminiModels.FLASH_2_5,
    var apiKey: String = "",
    var temperature: Float = 0.9F,
    var maxTokens: Int = 8192
)
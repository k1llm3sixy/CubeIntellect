package com.k1llm3sixy.cubeintellect.config

import dev.isxander.yacl3.api.NameableEnum
import net.minecraft.text.Text

enum class GeminiModels(val modelId: String) : NameableEnum {
    FLASH_2_5("gemini-2.5-flash"),
    FLASH_2_0("gemini-2.0-flash"),
    FLASH_1_5("gemini-1.5-flash"),
    PRO_2_5("gemini-2.5-pro");

    override fun getDisplayName(): Text {
        return Text.literal("§a$modelId")
    }
}

fun getModelsIds(): String {
    return GeminiModels.entries
        .joinToString("\n") { it.modelId }
}
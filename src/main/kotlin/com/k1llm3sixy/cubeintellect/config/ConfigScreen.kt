package com.k1llm3sixy.cubeintellect.config


import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG
import com.k1llm3sixy.cubeintellect.managers.ConfigManager
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class ConfigScreen : ConfigScreenFactory<Screen> {
    override fun create(parent: Screen?): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title(Text.literal(""))
            .category(
                ConfigCategory.createBuilder()
                    .name(Text.translatable("config.category.main"))
                    .option(
                        Option.createBuilder<String>()
                            .name(Text.translatable("config.option.api_key"))
                            .description(OptionDescription.of(Text.translatable("config.description.api_key", "aistudio.google.com/apikey")))
                            .binding(
                                "",
                                { CONFIG.apiKey },
                                { CONFIG.apiKey = it }
                            )
                            .controller(StringControllerBuilder::create)
                            .build()
                    )
                    .option(
                        Option.createBuilder<GeminiModels>()
                            .name(Text.translatable("config.option.gemini_model"))
                            .description(OptionDescription.of(Text.translatable("config.description.gemini_model", getModelsIds())))
                            .binding(
                                GeminiModels.FLASH_2_5,
                                { CONFIG.geminiModel },
                                { CONFIG.geminiModel = it }
                            )
                            .controller { EnumControllerBuilder.create(it).enumClass(GeminiModels::class.java) }
                            .build()
                    )
                    .option(
                        Option.createBuilder<Float>()
                            .name(Text.translatable("config.option.temperature"))
                            .description(OptionDescription.of(Text.translatable("config.description.temperature")))
                            .binding(
                                0.9F,
                                { CONFIG.temperature },
                                { CONFIG.temperature = it }
                            )
                            .controller { FloatSliderControllerBuilder.create(it)
                                .range(0.0F, 1.0F)
                                .step(0.1F)
                            }
                            .build()
                    )
                    .option(
                        Option.createBuilder<Int>()
                            .name(Text.translatable("config.option.max_tokens"))
                            .description(OptionDescription.of(Text.translatable("config.description.max_tokens")))
                            .binding(
                                8192,
                                { CONFIG.maxTokens },
                                { CONFIG.maxTokens = it }
                            )
                            .controller { IntegerSliderControllerBuilder.create(it)
                                .range(1, 8192)
                                .step(1)
                            }
                            .build()
                    )
                    .build()
            )
            .save(ConfigManager::save)
            .build()
            .generateScreen(parent)
    }
}
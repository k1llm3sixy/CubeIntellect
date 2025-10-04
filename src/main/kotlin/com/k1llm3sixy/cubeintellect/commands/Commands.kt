package com.k1llm3sixy.cubeintellect.commands

import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.sendMessage
import com.k1llm3sixy.cubeintellect.managers.GeminiManager
import com.k1llm3sixy.cubeintellect.config.ConfigScreen
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

const val CMD_MAIN = "cubeintellect"
const val CMD_QUESTION = "question"
const val CMD_SETTINGS = "settings"
object Commands {
    val scopeDef = CoroutineScope(Dispatchers.Default)
    val scopeIO = CoroutineScope(Dispatchers.IO)
    fun register() = ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ -> setupCommands(dispatcher) }

    fun setupCommands(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal(CMD_MAIN)
                .executes {
                    sendMessage(
                        Text.empty()
                            .append(Text.translatable("message.help_1"))
                            .append(Text.literal("\n"))
                            .append(Text.translatable("message.help_2"))
                    )
                    1
                }
                .then(
                    ClientCommandManager.literal(CMD_SETTINGS)
                        .executes {
                            openSettings()
                            1
                        }
                )
                .then(
                    ClientCommandManager.literal(CMD_QUESTION)
                        .then(
                            ClientCommandManager.argument("question", StringArgumentType.greedyString())
                                .executes { ctx ->
                                    val question = StringArgumentType.getString(ctx, "question")
                                    sendMessage(
                                        Text.empty()
                                            .append(Text.translatable("message.question", "§a$question"))
                                            .append(Text.literal("\n"))
                                            .append(Text.translatable("message.model", "§a${CONFIG.geminiModel.modelId}"))
                                            .append(Text.literal("\n"))
                                            .append(Text.translatable("message.temperature", "§a${CONFIG.temperature}"))
                                            .append(Text.literal("\n"))
                                            .append(Text.translatable("message.max_tokens", "§a${CONFIG.maxTokens}"))
                                    )
                                    scopeIO.launch { GeminiManager.handleQuestion(question) }
                                    1
                                }
                        )
                )
        )
    }

    fun openSettings() {
        val mc = MinecraftClient.getInstance()
        scopeDef.launch {
            delay(50)
            mc.execute {
                val screen = ConfigScreen().create(mc.currentScreen)
                mc.setScreen(screen)
            }
        }
    }
}
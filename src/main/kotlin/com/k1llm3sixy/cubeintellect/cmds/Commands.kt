package com.k1llm3sixy.cubeintellect.cmds

import com.k1llm3sixy.cubeintellect.ai.Parser
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import java.util.concurrent.CompletableFuture

class Commands {
    fun init() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            register(dispatcher)
        }
    }

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal("cubeintellect")
                .executes {
                    it.source.sendFeedback(
                        Text.literal(
                            """
                                §e/cubeintellect setKey <key> §7- here you can add your Gemini API key
                                §e/cubeintellect question <question> §7- here you can ask your question
                                §e/cubeintellect model <model> §7- here you can choose available models
                      """.trimIndent()
                        )
                    )
                    1
                }
                .then(
                    ClientCommandManager.literal("question")
                        .then(
                            ClientCommandManager.argument("question", StringArgumentType.greedyString())
                                .executes { context ->
                                    val question = StringArgumentType.getString(context, "question")
                                    context.source.sendFeedback(Text.literal("§6» §eYour question: §a$question"))
                                    Parser.parse(question, context.source)
                                    1
                                }
                        )
                )
                .then(
                    ClientCommandManager.literal("setKey")
                        .then(
                            ClientCommandManager.argument("key", StringArgumentType.greedyString())
                                .executes { context ->
                                    val key = StringArgumentType.getString(context, "key")
                                    context.source.sendFeedback(Text.literal("§6» Key §aadded"))
                                    Parser.key = key
                                    1
                                }
                        )
                )
                .then(
                    ClientCommandManager.literal("model")
                        .then(
                            ClientCommandManager.argument("model", StringArgumentType.greedyString())
                                .suggests(::suggestModels)
                                .executes { context ->
                                    val model = StringArgumentType.getString(context, "model")
                                    context.source.sendFeedback(Text.literal("§6» Choosen model - §a$model"))
                                    Parser.model = model
                                    1
                                }
                        )
                )
        )
    }

    fun suggestModels(
        ctx: CommandContext<FabricClientCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val modelList = listOf("gemini-2.0-flash", "gemini-2.5-flash", "gemini-1.5-flash")
        modelList.forEach { builder.suggest(it) }
        return builder.buildFuture()
    }
}
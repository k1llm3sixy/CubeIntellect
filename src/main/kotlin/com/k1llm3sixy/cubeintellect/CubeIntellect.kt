package com.k1llm3sixy.cubeintellect

import com.k1llm3sixy.cubeintellect.commands.Commands
import com.k1llm3sixy.cubeintellect.config.Config
import com.k1llm3sixy.cubeintellect.managers.ConfigManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

const val MOD_ID = "CubeIntellect"
class CubeIntellect : ClientModInitializer {
    companion object {
        lateinit var CONFIG: Config
        val CONFIG_FOLDER = "${FabricLoader.getInstance().configDir}/CubeIntellect/"
        val logger = LoggerFactory.getLogger(MOD_ID)
        fun sendMessage(text: Text) {
            val player = MinecraftClient.getInstance().player ?: return
            player.sendMessage(text, false)
        }
    }
    override fun onInitializeClient() {
        ConfigManager.createDirs()
        CONFIG = ConfigManager.load()
        Commands.register()
    }

}
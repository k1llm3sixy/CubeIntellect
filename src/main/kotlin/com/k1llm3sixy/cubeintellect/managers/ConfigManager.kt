package com.k1llm3sixy.cubeintellect.managers

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.CONFIG_FOLDER
import com.k1llm3sixy.cubeintellect.CubeIntellect.Companion.logger
import com.k1llm3sixy.cubeintellect.config.Config
import kotlinx.io.IOException
import java.io.File

const val CONFIG_NAME = "cubeintellect.json"
object ConfigManager {
    val configFile = File("$CONFIG_FOLDER$CONFIG_NAME")
    val gson = GsonBuilder().setPrettyPrinting().create()

    fun createDirs() {
        val file = File(CONFIG_FOLDER)
        if (!file.exists()) file.mkdirs()
        if (!configFile.exists()) configFile.createNewFile()
    }

    fun save() {
        try {
            configFile.writer().use { gson.toJson(CONFIG, it) }
        } catch (e: IOException) {
            logger.error("Failed to save configuration file!", e)
        }
    }

    fun load(): Config {
        return try {
            configFile.reader().use {
                gson.fromJson(it, Config::class.java) ?: Config()
            }
        } catch (e: IOException) {
            logger.error("Failed to load configuration file due to I/O error!", e)
            Config()
        } catch (e: JsonSyntaxException) {
            logger.error("Failed to load configuration file due to bad JSON syntax!", e)
            Config()
        }
    }
}
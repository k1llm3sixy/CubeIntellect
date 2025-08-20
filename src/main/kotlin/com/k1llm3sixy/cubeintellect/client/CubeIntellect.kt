package com.k1llm3sixy.cubeintellect.client

import com.k1llm3sixy.cubeintellect.cmds.Commands
import net.fabricmc.api.ClientModInitializer

class CubeIntellect: ClientModInitializer {
    override fun onInitializeClient() {
        Commands().init()
    }
}
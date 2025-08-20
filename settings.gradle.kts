pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        include("net.fabricmc:fabric-language-kotlin:1.13.3+kotlin.2.1.21")
    }
}
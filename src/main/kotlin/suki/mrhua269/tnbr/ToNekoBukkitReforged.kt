package suki.mrhua269.tnbr

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import suki.mrhua269.tnbr.command.impl.ToNekoGiveCommand
import suki.mrhua269.tnbr.command.impl.ToNekoSetNekoCommand
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.item.ItemRegistryManager
import suki.mrhua269.tnbr.item.impl.TestItem
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents
import suki.mrhua269.tnbr.metrics.Metrics

class ToNekoBukkitReforged : JavaPlugin() {
    companion object {
        lateinit var instance: ToNekoBukkitReforged
        lateinit var metrics: Metrics
        lateinit var config: Config
    }

    data class Config(
        val enableNightVisionPoison: Boolean,
        val enableIndependentHealthPoint: Boolean,
        val enableCreeperTargetingPrevent: Boolean
    )

    fun loadOtherConfig() {
        Companion.config = Config(
            enableNightVisionPoison = this.config.getBoolean("player_settings.neko.enable_night_vision_poison"),
            enableIndependentHealthPoint = this.config.getBoolean("player_settings.neko.enable_independent_health_point"),
            enableCreeperTargetingPrevent = this.config.getBoolean("player_settings.neko.enable_creeper_targeting_prevent")
        )
    }

    override fun onEnable() {
        instance = this
        metrics = Metrics(this, 27461)

        this.saveDefaultConfig()
        this.loadOtherConfig()
        this.loadLanguageFile()
        this.registerCommands()

        ItemRegistryManager.initAll {
            this.bootstrapItems()
        }

        Bukkit.getPluginManager().registerEvents(NekoPlayerEvents, this)
        Bukkit.getPluginManager().registerEvents(ItemRegistryManager, this)
    }

    private fun registerCommands() {
        ToNekoGiveCommand.register()
        ToNekoSetNekoCommand.register()
    }

    private fun bootstrapItems() {
        ItemRegistryManager.registerItem(TestItem())
    }

    private fun loadLanguageFile() {
        val targetLanguage = this.config.getString(ConstantPool.CONFIG_LANGUAGE_KEY)
            ?: throw NullPointerException("Language file could not be loaded as config could not be loaded.")

        I18NManager.loadLanguageData(targetLanguage)
    }

    override fun onDisable() {
        NekoPlayerEvents.saveAllPlayerData()
    }
}

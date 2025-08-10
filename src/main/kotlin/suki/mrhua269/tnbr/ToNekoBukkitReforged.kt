package suki.mrhua269.tnbr

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents

class ToNekoBukkitReforged : JavaPlugin() {
    companion object {
        lateinit var instance: ToNekoBukkitReforged
    }

    override fun onEnable() {
        instance = this

        this.saveDefaultConfig()
        this.loadLanguageFile()


        Bukkit.getPluginManager().registerEvents(NekoPlayerEvents, this)
    }

    private fun loadLanguageFile() {
        val targetLanguage = this.config.getString(ConstantPool.CONFIG_LANGUAGE_KEY)

        if (targetLanguage == null) {
            throw NullPointerException("Language file could not be loaded as config could not be loaded.")
        }

        I18NManager.loadLanguageData(targetLanguage)
    }

    override fun onDisable() {
        NekoPlayerEvents.saveAllPlayerData()
    }
}

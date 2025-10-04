package suki.mrhua269.tnbr.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.i18n.LanguageData

abstract class TabCompletableCommand(
    private val name: String,
) : BukkitCommand(name) {
    fun register() {
        Bukkit.getCommandMap().register(ToNekoBukkitReforged.instance.name, this)
    }

    fun notifyWrongUse(sender: CommandSender) {
        sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageWrongUse))
    }

    fun notifyPlayerNotFound(sender: CommandSender) {
        sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messagePlayerNotFound))
    }
}
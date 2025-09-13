package suki.mrhua269.tnbr.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import suki.mrhua269.tnbr.i18n.I18NManager

abstract class TabCompletableCommand(
    private val name: String,
) : BukkitCommand(name) {
    fun register() {
        Bukkit.getCommandMap().register(ToNekoBukkitReforged.instance.name, this)
    }
}
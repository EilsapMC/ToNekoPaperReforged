package suki.mrhua269.tnbr.command

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import suki.mrhua269.tnbr.ToNekoBukkitReforged

abstract class TabCompletableCommand : CommandExecutor, TabCompleter {
    fun register(name: String) {
        val pluginCommand = ToNekoBukkitReforged.instance.getCommand(name)

        if (pluginCommand == null) {
            throw IllegalArgumentException("Command $name not found")
        }

        pluginCommand.setExecutor(this)
        pluginCommand.tabCompleter = this
    }
}
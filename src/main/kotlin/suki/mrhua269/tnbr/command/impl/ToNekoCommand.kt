package suki.mrhua269.tnbr.command.impl

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import suki.mrhua269.tnbr.command.TabCompletableCommand

object ToNekoCommand : TabCompletableCommand() {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        return emptyList()
    }
}
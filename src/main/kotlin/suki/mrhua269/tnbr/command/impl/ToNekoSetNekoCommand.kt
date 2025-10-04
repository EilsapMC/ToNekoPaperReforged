package suki.mrhua269.tnbr.command.impl

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import suki.mrhua269.tnbr.command.TabCompletableCommand
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.i18n.LanguageData
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents.getPlayerNekoData

object ToNekoSetNekoCommand : TabCompletableCommand("setneko") {
    init {
        this.setDescription("Set a player to neko")
        this.setUsage("/tonekoset <player> <true/false>")
        this.permission = "toneko.command.set"
    }

    override fun tabComplete(
        sender: CommandSender,
        alies: String,
        args: Array<out String>
    ): List<String> {
        val argLen = args.size

        when (argLen) {
            1 -> {
                val result = mutableListOf<String>()

                for (player in Bukkit.getOnlinePlayers()) {
                    result.add(player.name)
                }

                return result
            }

            2 -> {
                return listOf("true", "false")
            }
        }

        return emptyList()
    }


    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>
    ): Boolean {
        // check permission
        if (!testPermission(sender)) {
            return true
        }

        if (args.size != 2) {
            this.notifyWrongUse(sender)
            return false
        }

        val target = args[0]
        val isNeko = args[1].toBooleanStrictOrNull()

        if (isNeko == null) {
            this.notifyWrongUse(sender)
            return false
        }

        val targetPlayer = sender.server.getPlayerExact(target)
        if (targetPlayer == null) {
            this.notifyPlayerNotFound(sender)
            return true
        }

        val targetNekoData = targetPlayer.getPlayerNekoData()
        val isNekoBefore = targetNekoData.isNeko
        targetNekoData.isNeko = isNeko

        val tagResolver = Placeholder.component("player", targetPlayer.displayName())

        if (isNekoBefore && !isNeko) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageNekoSetToNotANeko, tagResolver))
            return true
        }

        if (!isNekoBefore && isNeko) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageNekoSetToANeko, tagResolver))
            return true
        }

        sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageNothingHasChanged))
        return true
    }
}
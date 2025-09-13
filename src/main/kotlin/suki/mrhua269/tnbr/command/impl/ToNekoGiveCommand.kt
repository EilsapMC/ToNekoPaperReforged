package suki.mrhua269.tnbr.command.impl

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import suki.mrhua269.tnbr.command.TabCompletableCommand
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.i18n.LanguageData
import suki.mrhua269.tnbr.item.ItemRegistryManager

object ToNekoGiveCommand : TabCompletableCommand("tonekogive") {
    init {
        this.setDescription("Give player something of toneko items")
        this.setUsage("/tonekogive <player> <item>")
        this.permission = "toneko.give"
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
                val result = mutableListOf<String>()

                for (registed in ItemRegistryManager.getAllManaged()) {
                    result.add(registed.toString())
                }

                return result
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

        if (args.size < 2) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageWrongUse))
            return false
        }

        val playerName = args[0]
        val itemRegistry = args[1]
        val itemRegistryObj = NamespacedKey.fromString(itemRegistry)

        if (itemRegistryObj == null) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageWrongUse))
            return false
        }

        val targetPlayer = Bukkit.getPlayer(playerName)

        if (targetPlayer == null) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messagePlayerNotFound))
            return true
        }

        val targetItem = ItemRegistryManager.createItem(itemRegistryObj)

        if (targetItem == null) {
            sender.sendMessage(LanguageData.miniMessaged(I18NManager.getLanguageData().messageItemNotFoundGiveCommand))
            return true
        }

        targetPlayer.scheduler.execute(ToNekoBukkitReforged.instance, {
            targetPlayer.give(targetItem)
        }, null, 1L)
        return true
    }
}
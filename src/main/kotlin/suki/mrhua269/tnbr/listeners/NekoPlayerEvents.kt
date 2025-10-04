package suki.mrhua269.tnbr.listeners

import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.storage.NekoPlayerData
import suki.mrhua269.tnbr.tasks.NekoPlayerBindTask
import suki.mrhua269.tnbr.utils.dispatchers.EntityDispatcherImpl
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

object NekoPlayerEvents : Listener{
    private val playerChatRenderNeko = PlayerChatRenderNeko()
    private val playerNekoDataCachedMap : MutableMap<UUID, NekoPlayerData> = ConcurrentHashMap()
    private val playerNekoBindTasks: MutableMap<UUID, NekoPlayerBindTask> = ConcurrentHashMap()

    class PlayerChatRenderNeko : ChatRenderer {
        override fun render(
            source: Player,
            sourceDisplayName: Component,
            message: Component,
            viewer: Audience
        ): Component {
            val languageData = I18NManager.getLanguageData()
            val playerNekoPlayerData = source.getPlayerNekoData()

            var replaceConfigBuilder = TextReplacementConfig.builder()
            var shouldReplace = false
            var masters = ""

            // this would be thread safe as we will copy the owners for each modification
            for (ownerUUID in playerNekoPlayerData.owners) {
                val target = Bukkit.getOfflinePlayer(ownerUUID)

                // skip if missing
                if (target.name != null && target.name!!.isBlank() ) continue

                target.name?.let {
                    shouldReplace = true
                    // add to its master list will be present in the hover message
                    masters += (it + "\t")

                    // replace with master word
                    replaceConfigBuilder = replaceConfigBuilder.matchLiteral(it)
                }
            }

            var replaced = message
            // if we detected the owner's name in its chat message
            if (shouldReplace) {
                // meow?
                replaceConfigBuilder.replacement(languageData.messageMasterWord)

                val builtReplaceConfig = replaceConfigBuilder.build()
                replaced = replaced.replaceText(builtReplaceConfig)
            }

            // append meow suffix
            val builtMessage = replaced
                .append(Component.text(languageData.messageNekoPlayerMeow))

            // build master list component
            val mastersComponent = masters.let {
                if (it.isBlank()) {
                    return@let Component.text(languageData.messageHoverEventOfNekoNoMaster)
                }

                return@let Component.text(it)
            }

            // we need process its prefix from mini message to a true component
            val processedHoverMessage = MiniMessage.miniMessage().deserialize(
                languageData.messageHoverEventOfNekoPrefix,
                Placeholder.component("masters", mastersComponent)
            )
            val processedPrefix = MiniMessage.miniMessage()
                .deserialize(languageData.messageNekoPlayerPrefix)
                .hoverEvent(HoverEvent.showText(processedHoverMessage))

            val result = MiniMessage.miniMessage().deserialize(
                languageData.messageNekoPlayerChatFormat,
                Placeholder.component("prefix", processedPrefix),
                Placeholder.component("player_display_name", sourceDisplayName),
                Placeholder.component("message", builtMessage)
            )

            return result
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player
        // read player data
        val nekoPlayerData = this.playerNekoDataCachedMap.computeIfAbsent(player.uniqueId){
            NekoPlayerData()
        }

        nekoPlayerData.restoreFrom(player)

        // create tick task for player
        val playerNekoTickTask = this.playerNekoBindTasks.computeIfAbsent(player.uniqueId, Function<UUID, NekoPlayerBindTask>{
            NekoPlayerBindTask(player)
        })

        playerNekoTickTask.schedule()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        val player = event.player
        val nekoPlayerData = this.playerNekoDataCachedMap[player.uniqueId]

        if (nekoPlayerData != null) {
            nekoPlayerData.storeTo(player)

            this.playerNekoDataCachedMap.remove(player.uniqueId)
        }

        this.playerNekoBindTasks.remove(player.uniqueId)?.retire()
    }

    fun getPlayerNekoDataRaw(player: Player): NekoPlayerData = this.playerNekoDataCachedMap.computeIfAbsent(player.uniqueId){ NekoPlayerData() }

    fun Player.getPlayerNekoData(): NekoPlayerData = getPlayerNekoDataRaw(this)

    fun saveAllPlayerData() {
        runBlocking {
            for (player in Bukkit.getOnlinePlayers()) {
                // we might get descheduled when the player gose offline when we're doing that, so just do it async
                async(EntityDispatcherImpl(player, true)) {
                    val playerNekoData = playerNekoDataCachedMap[player.uniqueId]

                    if (!Bukkit.isOwnedByCurrentRegion(player)) {
                        return@async
                    }

                    playerNekoData?.storeTo(player)
                }
            }
        }
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val nekoPlayerData = event.player.getPlayerNekoData()

        // skip chat message replacement for non neko players
        if (!nekoPlayerData.isNeko) {
            return
        }

        event.renderer(this.playerChatRenderNeko)
    }

    @EventHandler
    fun onCreeperTargetNekoPlayer(event: EntityTargetEvent) {
        val targetReason = event.reason
        val targetEntity = event.target

        targetEntity?.let {
            if (targetReason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER) {
                val player = targetEntity as Player
                val playerNekoData = player.getPlayerNekoData()

                if (playerNekoData.isNeko) {
                    event.isCancelled = true
                }
            }
        }
    }
}
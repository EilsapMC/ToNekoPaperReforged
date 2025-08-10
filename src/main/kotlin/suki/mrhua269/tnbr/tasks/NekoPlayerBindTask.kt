package suki.mrhua269.tnbr.tasks

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents.getPlayerNekoData
import java.util.function.Consumer

class NekoPlayerBindTask(
    private val player: Player
) : Consumer<ScheduledTask>{
    private var scheduledTask: ScheduledTask? = null
    private var isNekoBefore: Boolean = false

    fun schedule() {
        this.scheduledTask = this.player.scheduler.runAtFixedRate(
            ToNekoBukkitReforged.instance,
            this,
            null,
            1L,
            1L
        )
    }

    fun retire() {
        this.scheduledTask?.cancel()
    }

    override fun accept(t: ScheduledTask) {
        val playerNekoPlayerData = this.player.getPlayerNekoData()

        if (playerNekoPlayerData.isNeko) {
            this.tickPotionEffect()
        }

        if (!playerNekoPlayerData.isNeko && this.isNekoBefore) {
            this.cleanNekoStats()
        }

        this.isNekoBefore = playerNekoPlayerData.isNeko
    }

    internal fun cleanNekoStats() {
        this.player.removePotionEffect(PotionEffectType.NIGHT_VISION)
    }

    internal fun tickPotionEffect() {
        val nightVisionEffect = PotionEffectType.NIGHT_VISION
        val duration = 1000
        val amplifier = 4

        val potionEffect = PotionEffect(nightVisionEffect, duration, amplifier, false, false)
        this.player.addPotionEffect(potionEffect)
    }
}
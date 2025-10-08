package suki.mrhua269.tnbr.tasks

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import suki.mrhua269.tnbr.ConstantPool
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents.getPlayerNekoData
import java.util.function.Consumer

class NekoPlayerBindTask(
    private val player: Player
) : Consumer<ScheduledTask>{
    private var scheduledTask: ScheduledTask? = null
    private var isNekoBefore: Boolean = false
    private var oldMaxHealth = 0.0

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

        // handle stats upgrade / downgrade
        if (playerNekoPlayerData.isNeko && !this.isNekoBefore) {
            this.prepareNekoStats()
        }

        if (!playerNekoPlayerData.isNeko && this.isNekoBefore) {
            this.cleanNekoStats()
        }

        // tick logics
        if (playerNekoPlayerData.isNeko) {
            this.tickPotionEffect()
        }

        this.isNekoBefore = playerNekoPlayerData.isNeko
    }

    internal fun prepareNekoStats() {
        if (ToNekoBukkitReforged.config.enableIndependentHealthPoint) {
            // set to max health of neko player
            val attributeInstanceOfMaxHealth = this.player.getAttribute(Attribute.MAX_HEALTH)

            if (attributeInstanceOfMaxHealth != null) {
                this.oldMaxHealth = attributeInstanceOfMaxHealth.baseValue
                attributeInstanceOfMaxHealth.baseValue = ConstantPool.NEKO_PLAYER_MAX_HEALTH
            }
        }
    }

    internal fun cleanNekoStats() {
        this.player.removePotionEffect(PotionEffectType.NIGHT_VISION)

        // reset max health to default
        val attributeInstanceOfMaxHealth = this.player.getAttribute(Attribute.MAX_HEALTH)

        if (attributeInstanceOfMaxHealth != null && ToNekoBukkitReforged.config.enableIndependentHealthPoint) {
            attributeInstanceOfMaxHealth.baseValue = this.oldMaxHealth
        }
    }

    internal fun tickPotionEffect() {
        if (ToNekoBukkitReforged.config.enableNightVisionPoison) {
            val nightVisionEffect = PotionEffectType.NIGHT_VISION
            val duration = 1000
            val amplifier = 4

            val potionEffect = PotionEffect(nightVisionEffect, duration, amplifier, false, false)
            this.player.addPotionEffect(potionEffect)
        }
    }
}
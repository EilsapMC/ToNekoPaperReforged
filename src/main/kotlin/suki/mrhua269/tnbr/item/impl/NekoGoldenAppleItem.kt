package suki.mrhua269.tnbr.item.impl

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import suki.mrhua269.tnbr.i18n.I18NManager
import suki.mrhua269.tnbr.item.ItemRegistryEntry
import suki.mrhua269.tnbr.listeners.NekoPlayerEvents.getPlayerNekoData

class NekoGoldenAppleItem: ItemRegistryEntry() {
    override fun getMaterial(): Material {
        return Material.ENCHANTED_GOLDEN_APPLE
    }

    override fun getRegistryId(): String {
        return "tnbr:neko_golden_apple"
    }

    override fun getCustomItemMeta(item: ItemStack): ItemMeta {
        val copied = item.itemMeta.clone()

        copied.customName(MiniMessage.miniMessage().deserialize(
            I18NManager.getLanguageData().constantNekoGoldenAppleName
        ))

        copied.lore(listOf(MiniMessage.miniMessage().deserialize(
            I18NManager.getLanguageData().constantNekoGoldenAppleLore
        )))

        return copied;
    }

    override fun onUse(interactEvent: PlayerInteractEvent) {

    }

    override fun onConsume(consumeEvent: PlayerItemConsumeEvent) {
        val player = consumeEvent.player
        val nekoData = player.getPlayerNekoData()

        if (nekoData.trySetNeko(true)) {
            player.playSound(player, Sound.ENTITY_CAT_AMBIENT, 1f, 1f)
            player.spawnParticle(Particle.EXPLOSION, player.location, 1)
        }
    }

    override fun fillItemData(
        container: PersistentDataContainer,
        item: ItemStack
    ) {
    }

    override fun getRecipe(): Recipe? {
        return null
    }
}
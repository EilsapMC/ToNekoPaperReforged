package suki.mrhua269.tnbr.item.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFactory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import suki.mrhua269.tnbr.item.ItemRegistryEntry

class TestItem: ItemRegistryEntry() {
    override fun getMaterial(): Material {
        return Material.END_ROD
    }

    override fun getRegistryId(): String {
        return "justarod:giant_rod"
    }

    override fun getCustomItemMeta(item: ItemStack): ItemMeta? {
        val meta = Bukkit.getItemFactory().getItemMeta(item.type)
        meta.itemModel = NamespacedKey.fromString("justarod:item/giant_rod")
        return meta
    }

    override fun onUse(interactEvent: PlayerInteractEvent) {
        val location = interactEvent.player.location

        location.world.spawnParticle(Particle.HEART, location, 10, 2.0, 2.0, 2.0)
    }

    override fun fillItemData(
        container: PersistentDataContainer,
        item: ItemStack
    ) {
        item.lore(listOf(Component.text("喵?!").color(TextColor.fromHexString("#f5c2e7"))))
    }

    override fun getRecipe(): Recipe? {
        return null
    }
}
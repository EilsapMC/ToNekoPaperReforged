package suki.mrhua269.tnbr.item

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

object ItemRegistryManager : Listener {
    private val managedRegistryEntries = ConcurrentHashMap<NamespacedKey, ItemRegistryEntry>()

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteractEvent(playerInteractEvent: PlayerInteractEvent) {
        val usedItem = playerInteractEvent.item ?: return
        val groupRegistry = ItemRegistryEntry.getGroupIdIfHave(usedItem) ?: return

        val lookedup = this.managedRegistryEntries[groupRegistry] ?: return

        lookedup.onUse(playerInteractEvent)
    }

    fun registerItem(entry: ItemRegistryEntry) {
        val key = NamespacedKey.fromString(entry.getRegistryId())
            ?: throw IllegalArgumentException("Invalid key " + entry.getRegistryId())

        this.managedRegistryEntries[key] = entry
    }

    fun createItem(registryKey: NamespacedKey): ItemStack? {
        val lookedup = this.managedRegistryEntries[registryKey] ?: return null

        return lookedup.createItem0()
    }

    fun getAllManaged(): List<NamespacedKey> {
        return managedRegistryEntries.keys.toList()
    }

    fun initAll(addAction: Runnable) {
        addAction.run()

        // registry recipes
        for (entry in managedRegistryEntries.values) {
            val recipe = entry.getRecipe()

            if (recipe != null) {
                Bukkit.addRecipe(recipe)
            }
        }
    }
}
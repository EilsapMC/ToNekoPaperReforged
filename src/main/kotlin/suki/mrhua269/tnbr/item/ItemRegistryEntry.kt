package suki.mrhua269.tnbr.item

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import suki.mrhua269.tnbr.ConstantPool

abstract class ItemRegistryEntry {
    companion object {
        private val ITEM_DATA_PARENT_KEY = warpNamespaceKey(true, "")
        private val ITEM_GROUP_ID = warpNamespaceKey(false, "item_group_id")

        private fun warpNamespaceKey(root: Boolean, endPath: String): NamespacedKey{
            return  NamespacedKey(
                ConstantPool.TO_NEKO_RESOURCE_KEY,
                if (root) ConstantPool.TO_NEKO_ITEM_DATA_RESOURCE_LOCATION_BASE else endPath
            )
        }

        fun getGroupIdIfHave(itemStack: ItemStack): NamespacedKey? {
            val container = itemStack.persistentDataContainer
            val metaContainer = container.get(ITEM_DATA_PARENT_KEY, PersistentDataType.TAG_CONTAINER) ?: return null

            val groupId = metaContainer.get(ITEM_GROUP_ID, PersistentDataType.STRING) ?: return null

            return NamespacedKey.fromString(groupId)
        }
    }

    fun createItem0(): ItemStack {
        val ret = ItemStack(this.getMaterial())

        this.fillItemMeta(ret)

        return ret
    }

    abstract fun getMaterial(): Material

    abstract fun getRegistryId(): String

    abstract fun getCustomItemMeta(item: ItemStack): ItemMeta?

    abstract fun onUse(interactEvent: PlayerInteractEvent)

    abstract fun fillItemData(container: PersistentDataContainer, item: ItemStack)

    abstract fun getRecipe(): Recipe?

    fun  fillItemMeta(itemStack: ItemStack) {
        var itemMetaContainer = itemStack.persistentDataContainer.get(ITEM_DATA_PARENT_KEY, PersistentDataType.TAG_CONTAINER)

        if (itemMetaContainer != null) {
            throw IllegalStateException("Given item already has an meta!")
        }

        itemMetaContainer = itemStack.persistentDataContainer.adapterContext.newPersistentDataContainer()

        this.fillItemMetaContainer(itemMetaContainer, itemStack)

        itemStack.editPersistentDataContainer { persistentDataContainer ->
            persistentDataContainer.set(ITEM_DATA_PARENT_KEY, PersistentDataType.TAG_CONTAINER, itemMetaContainer)
        }
    }

    fun fillItemMetaContainer(container: PersistentDataContainer, itemStack: ItemStack) {
        val registry = this.getRegistryId()

        container.set(ITEM_GROUP_ID, PersistentDataType.STRING, registry)

        this.getCustomItemMeta(itemStack)?.let {
            itemStack.itemMeta = it
        }

        this.fillItemData(container, itemStack)
    }

}
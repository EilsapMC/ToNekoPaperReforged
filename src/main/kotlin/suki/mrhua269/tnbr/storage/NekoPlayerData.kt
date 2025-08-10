package suki.mrhua269.tnbr.storage

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import suki.mrhua269.tnbr.ConstantPool
import suki.mrhua269.tnbr.utils.UUIDPersistentDataType
import java.util.UUID

class NekoPlayerData {
    companion object{
        private val NEKO_PLAYER_DATA_KEY = warpNamespaceKey(true, "")
        private val IS_NEKO_SPACE_KEY = warpNamespaceKey(false, "is_neko")

        private val OWNERS_SPACE_KEY = warpNamespaceKey(false, "owners")
        private val OWNERS_DATA_TYPE = PersistentDataType.LIST.listTypeFrom(UUIDPersistentDataType.INSTANCE)

        private fun warpNamespaceKey(root: Boolean, endPath: String): NamespacedKey{
            return  NamespacedKey(
                ConstantPool.TO_NEKO_RESOURCE_KEY,
                if (root) ConstantPool.TO_NEKO_PLAYER_DATA_RESOURCE_LOCATION_BASE else endPath
            )
        }
    }

    // the data we're maintaining allows concurrent read but mt write, so added synchronized around those write
    // operations
    @Volatile
    var isNeko: Boolean = false
    @Volatile
    var owners: List<UUID> = emptyList()

    fun setNeko(isNeko: Boolean): NekoPlayerData {
        this.isNeko = isNeko
        return this
    }

    @Synchronized
    fun addOwner(uuid: UUID): NekoPlayerData {
        this.owners += uuid
        return this
    }

    @Synchronized
    fun removeOwner(uuid: UUID): NekoPlayerData {
        this.owners -= uuid
        return this
    }

    fun storeTo(player: Player) {
        // main thread checks
        if (!Bukkit.isOwnedByCurrentRegion(player)) {
            throw IllegalStateException("Save player neko data off-main!")
        }

        val dataContainer = player.persistentDataContainer
        val nekoPlayerDataContainer =  player.persistentDataContainer.adapterContext.newPersistentDataContainer()

        this.saveMetaToContainer(nekoPlayerDataContainer)

        dataContainer.set(NEKO_PLAYER_DATA_KEY, PersistentDataType.TAG_CONTAINER, nekoPlayerDataContainer)
    }

    private fun saveMetaToContainer(container: PersistentDataContainer){
        container.set(IS_NEKO_SPACE_KEY, PersistentDataType.BOOLEAN, this.isNeko)

        val owners = this.owners
        container.set(OWNERS_SPACE_KEY, OWNERS_DATA_TYPE, owners)
    }

    fun restoreFrom(player: Player) {
        // main thread checks
        if (!Bukkit.isOwnedByCurrentRegion(player)) {
            throw IllegalStateException("Load player neko data off-main!")
        }

        val dataContainer = player.persistentDataContainer
        val nekoPlayerDataContainer = dataContainer.get(NEKO_PLAYER_DATA_KEY, PersistentDataType.TAG_CONTAINER)

        nekoPlayerDataContainer?.let {
            this.loadMetaFromContainer(it)
            return
        }

        // not found, we'll create for it
        this.storeTo(player)
    }

    private fun loadMetaFromContainer(container: PersistentDataContainer) {
        this.isNeko = container.get(IS_NEKO_SPACE_KEY, PersistentDataType.BOOLEAN)!!
        this.owners = container.get(OWNERS_SPACE_KEY, OWNERS_DATA_TYPE).let {
            if (it == null) {
                return@let emptyList()
            }

            return@let it
        }
    }
}
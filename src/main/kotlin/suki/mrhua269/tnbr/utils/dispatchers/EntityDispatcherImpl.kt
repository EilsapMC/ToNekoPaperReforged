package suki.mrhua269.tnbr.utils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import kotlin.coroutines.CoroutineContext

class EntityDispatcherImpl(
    private val entity: Entity
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        this.entity.scheduler.execute(ToNekoBukkitReforged.instance, block, null, 1L)
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return Bukkit.isOwnedByCurrentRegion(this.entity)
    }
}
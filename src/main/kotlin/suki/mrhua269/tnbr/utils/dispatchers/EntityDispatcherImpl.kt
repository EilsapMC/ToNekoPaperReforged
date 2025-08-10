package suki.mrhua269.tnbr.utils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import suki.mrhua269.tnbr.ToNekoBukkitReforged
import kotlin.coroutines.CoroutineContext

class EntityDispatcherImpl(
    private val entity: Entity,
    private val runEvenRetired: Boolean
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.getServer().isStopping) {
            block.run()
            return
        }

        val retired = if (this.runEvenRetired) block else null

        val result = this.entity.scheduler.execute(ToNekoBukkitReforged.instance, block, retired, 1L)

        if (!result && this.runEvenRetired) {
            block.run()
        }
    }
}
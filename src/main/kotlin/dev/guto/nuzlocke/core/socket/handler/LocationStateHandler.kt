package dev.guto.nuzlocke.core.socket.handler

import dev.guto.nuzlocke.core.data.ram.WarpData
import dev.guto.nuzlocke.core.event.MessageChannel
import dev.guto.nuzlocke.util.ByteArrayExtension.Companion.toIntLE
import dev.guto.nuzlocke.util.ByteArrayExtension.Companion.toShortLE

class LocationStateHandler(private val locationChannel: MessageChannel<WarpData>) {
    fun notifyLocationStateChange(current: ByteArray, new: ByteArray): ByteArray {
        if (!current.contentEquals(new)) {
            println("Notifying location change.")
            val data = WarpData(
                new.toShortLE(),
                new.toShortLE(1),
                new.toShortLE(2),
                new.toIntLE(3),
                new.toIntLE(5)
            )
            locationChannel.produce(
                data
            )
        }
        return new
    }
}
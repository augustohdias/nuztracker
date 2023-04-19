package dev.guto.nuzlocke.core.socket.handler

import dev.guto.nuzlocke.core.event.MessageChannel
import dev.guto.nuzlocke.util.encoding.PokemonEncoding.Companion.decode

class NameStateHandler(
    private val nameChannel: MessageChannel<String>
) {
    fun notifyNameStateChange(currentState: ByteArray, newState: ByteArray): ByteArray {
        if (!currentState.contentEquals(newState)) {
            println("Name changed. New game detected.")
            nameChannel.produce(
                newState.decode()
            )
        }
        return newState
    }
}
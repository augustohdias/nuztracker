package dev.guto.nuzlocke.core.socket.handler

import dev.guto.nuzlocke.core.event.MessageChannel

class CommandHandler(private val commandChannel: MessageChannel<String>) {
    fun notifyCommand(currentState: ByteArray, newState: ByteArray): ByteArray {
        commandChannel.produce("SAV")
        return newState
    }
}

package dev.guto.nuzlocke.core.socket.handler

import dev.guto.nuzlocke.core.data.ram.Pokedex
import dev.guto.nuzlocke.core.event.MessageChannel

class PokemonSeenHandler(private val pokedexSeenChannel: MessageChannel<Pokedex>) {
    fun notifyPokedexSeenStateChange(current: ByteArray, new: ByteArray): ByteArray {
        if (!current.contentEquals(new)) {
            println("Notifying seen pokemon change.")
            pokedexSeenChannel.produce(Pokedex(new))
        }
        return new
    }
}
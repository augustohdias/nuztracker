package dev.guto.nuzlocke.core.socket.handler

import dev.guto.nuzlocke.core.data.static.Pokemon
import dev.guto.nuzlocke.core.event.MessageChannel
import dev.guto.nuzlocke.util.encoding.PokemonEncoding.Companion.decode

class PokemonCaughtHandler(private val pokedexCaughtChannel: MessageChannel<Pokemon>) {
    fun notifyPokedexCaughtStateChange(current: ByteArray, new: ByteArray): ByteArray {
        if (!current.contentEquals(new)) {
            val decodedMessage = new.decode()
            if (decodedMessage.contains("Gotcha!")) {
                println("Notifying caught pokemon change.")
                pokedexCaughtChannel.produce(parse(decodedMessage))
            }
        }
        return new
    }

    private fun parse(message: String): Pokemon {
        val pokemonName = message
            .replace("Gotcha!\n", "")
            .split(" ")
            .getOrElse(0) { Pokemon.MissingNo.name }
        return Pokemon.findByName(pokemonName)
    }
}
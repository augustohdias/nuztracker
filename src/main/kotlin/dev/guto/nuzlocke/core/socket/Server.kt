package dev.guto.nuzlocke.core.socket

import dev.guto.nuzlocke.core.data.ram.Pokedex
import dev.guto.nuzlocke.core.data.ram.WarpData
import dev.guto.nuzlocke.core.data.static.Pokemon
import dev.guto.nuzlocke.core.event.EventBroker
import dev.guto.nuzlocke.core.event.MessageChannel
import dev.guto.nuzlocke.core.socket.handler.*
import dev.guto.nuzlocke.core.socket.messages.Headers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.NullPointerException
import java.net.ServerSocket

class Server(
    broker: EventBroker
) : Thread() {

    override fun run() {
        while (true) {
            println("Starting new socket server.")
            val serverSocket = ServerSocket(25322)
            println("Listening on port 25322!!")
            val socket = serverSocket.accept()
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            runCatching {
                parseMessages(input)
            }.onFailure {
                println("Connection lost")
                println("Closing previous socket server")
                it.printStackTrace()
                serverSocket.close()
            }
        }
    }

    private fun parseMessages(input: BufferedReader) {
        val currentState = INITIAL_STATE.toMutableMap().withDefault { byteArrayOf() }
        while (true) {
            val message = input.readLine() ?: "NULL|00"
            val (header, body) = message.split("|")
            currentState[header] =
                handlers.getValue(Headers.parse(header))(
                    currentState.getValue(header),
                    body.hexStringBytesToByteArray()
                )
        }
    }

    private val handlers = mapOf<Headers, (ByteArray, ByteArray) -> ByteArray>(
        Headers.PXC to PokemonCaughtHandler(broker.caughtChannel)::notifyPokedexCaughtStateChange,
        Headers.PXS to PokemonSeenHandler(broker.seenChannel)::notifyPokedexSeenStateChange,
        Headers.LOC to LocationStateHandler(broker.locationChannel)::notifyLocationStateChange,
        Headers.NAM to NameStateHandler(broker.nameChannel)::notifyNameStateChange,
        Headers.SAV to CommandHandler(broker.commandChannel)::notifyCommand,
    ).withDefault {
        this::consume
    }

    private fun consume(unused: ByteArray, unused2: ByteArray) = byteArrayOf()

    companion object {
        private fun String.hexStringBytesToByteArray(): ByteArray {
            return this.split(' ').filterNot { it == "" }.map { it.toInt(16).toByte() }.toByteArray()
        }

        val INITIAL_STATE =
            mapOf(
                "PXC" to byteArrayOf(),
                "PXS" to byteArrayOf(),
                "LOC" to byteArrayOf(),
                "NAM" to byteArrayOf(),
            ).withDefault { byteArrayOf() }
    }
}
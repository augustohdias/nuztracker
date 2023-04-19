package dev.guto.nuzlocke.core

import com.fasterxml.jackson.databind.ObjectMapper
import dev.guto.nuzlocke.core.data.ram.Pokedex
import dev.guto.nuzlocke.core.data.ram.WarpData
import dev.guto.nuzlocke.core.data.static.Pokemon
import dev.guto.nuzlocke.core.event.EventBroker
import dev.guto.nuzlocke.core.socket.messages.Headers
import java.io.File

class Core(
    private val broker: EventBroker
) : Thread() {
    companion object {
        val objectMapper = ObjectMapper()
    }

    var trainerName = ""
    var seen = Pokedex(byteArrayOf())
    var currentLocation = WarpData(0, 0, 0, 0, 0)
    private val tracking = mutableMapOf<String, MutableList<String>>()

    @Volatile
    var lastCaptured = Pokemon.Abra
    private var trackFile: File = File("dmp")

    override fun run() {
        while (true) {
            broker.commandChannel.consume().ifPresent {
                handleCommand(it)
            }

            broker.nameChannel.consume().ifPresent {
                handleReset(it)
            }

            broker.locationChannel.consume().ifPresent {
                handleLocationChange(it)
            }

            broker.caughtChannel.consume().ifPresent {
                handleCaughtPokemon(it)
            }

            broker.seenChannel.consume().ifPresent {
                handleSeenPokemon(it)
            }
        }
    }

    private fun handleCommand(it: String) {
        when (it) {
            Headers.SAV.name -> if (trainerName.isNotEmpty()) {
                println("Saving state.")
                printTrack()
                updateTrackFile()
                println("Saved.")
            }
        }
    }

    private fun handleSeenPokemon(it: Pokedex) {
        println("Seen: ${it.pokemons().minus(seen.pokemons())}")
        seen = it
    }

    private fun handleCaughtPokemon(it: Pokemon) {
        println("${it.name} caught @ ${currentLocation.locationName}")
        if (tracking[currentLocation.locationName] == null) {
            tracking[currentLocation.locationName] = mutableListOf(it.name)
        } else {
            tracking[currentLocation.locationName]?.add(it.name)
        }
        lastCaptured = it
    }

    private fun handleLocationChange(it: WarpData) {
        currentLocation = it
        println("Location changed: ${it.locationName}")
        printTrack()
    }

    private fun handleReset(it: String) {
        trainerName = it
        seen = Pokedex(byteArrayOf())
        tracking.clear()
        trackFile = File("$trainerName.json")
        if (!trackFile.exists()) {
            trackFile.createNewFile()
            trackFile.writeText("{}")
        } else {
            println("Loading previous state...")
            val previousTrack =
                objectMapper.readValue(
                    File("$trainerName.json").readText(),
                    Map::class.java
                ) as Map<String, List<String>>
            previousTrack.forEach { entry -> tracking[entry.key] = entry.value.toMutableList() }
            printTrack()
        }
    }

    private fun printTrack() {
        println(tracking.map { "- ${it.value} caught @ ${it.key}" }.joinToString("\n"))
    }

    private fun updateTrackFile() {
        if (!trackFile.exists()) {
            trackFile.createNewFile()
        }
        trackFile.writeText(objectMapper.writeValueAsString(tracking))
    }

    fun getTracking(): Map<String, List<String>> {
        return tracking.toMap()
            .map { it.key to it.value.map { p -> p } }.toMap()
            .withDefault { mutableListOf(Pokemon.MissingNo.name) }
    }
}
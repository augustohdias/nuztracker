package dev.guto.nuzlocke.core.event

import dev.guto.nuzlocke.core.data.ram.Pokedex
import dev.guto.nuzlocke.core.data.ram.WarpData
import dev.guto.nuzlocke.core.data.static.Pokemon

class EventBroker {
    val caughtChannel = MessageChannel<Pokemon>()
    val seenChannel = MessageChannel<Pokedex>()
    val locationChannel = MessageChannel<WarpData>()
    val nameChannel = MessageChannel<String>()
    val commandChannel = MessageChannel<String>()
    val trackChannel = MessageChannel<Map<String,List<Pokemon>>>()
}
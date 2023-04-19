package dev.guto.nuzlocke.core.data.ram

import dev.guto.nuzlocke.core.data.static.Pokemon

class Pokedex() {
    companion object {
        const val GEN_III_SIZE = 424
    }

    constructor(flags: ByteArray) : this() {
        this.flags = flags

        for (i in 1 until GEN_III_SIZE) {
            if (contains(i)) {
                pokemons.add(Pokemon.find(i))
            }
        }
    }

    private lateinit var flags: ByteArray
    private val pokemons = mutableSetOf<Pokemon>()

    private fun contains(nationalDexNumber: Int): Boolean {
        if (flags.isEmpty()) return false
        val number = (nationalDexNumber - 1)
        return (((flags[number shr 3]).toInt() shr (number and 7)) and 1) == 1
    }

    fun pokemons() = pokemons.toSet()

    fun pokemons(pokemons: Set<Pokemon>): Set<Pokemon> {
        this.pokemons.clear()
        this.pokemons.addAll(pokemons)
        return pokemons
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pokedex

        if (!flags.contentEquals(other.flags)) return false

        return true
    }

    override fun hashCode(): Int {
        return flags.contentHashCode()
    }
}
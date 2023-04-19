package dev.guto.nuzlocke.core.data.static

class Locations {
    companion object {
        private val MAP = mapOf(
            (0 to 0) to "Petalburg City",
            (0 to 8) to "Slateport City",
            (0 to 16) to "Mauville City",
            (0 to 24) to "Rustboro City",
            (0 to 32) to "Fortree City",
            (0 to 40) to "Lilycove City",
            (0 to 48) to "Mossdeep City",
            (0 to 56) to "Sootopolis City",
            (0 to 64) to "Ever Grande City",
            (0 to 72) to "Littleroot Town",
            (0 to 80) to "Oldale Town",
            (0 to 88) to "Dewford Town",
            (0 to 96) to "Lavardige Town",
            (0 to 104) to "Fallarbor Town",
            (0 to 112) to "Vedanturf Town",
            (0 to 120) to "Pacifidlog Town",
            (0 to 128) to "Route 101",
            (0 to 136) to "Route 102",
            (0 to 144) to "Route 103",
            (0 to 152) to "Route 104",
            (0 to 160) to "Route 105",
            (0 to 168) to "Route 106",
            (0 to 176) to "Route 107",
            (0 to 184) to "Route 108",
            (0 to 192) to "Route 109",
            (0 to 200) to "Route 110",
            (0 to 208) to "Route 111",
            (0 to 216) to "Route 112",
            (0 to 224) to "Route 113",
            (0 to 232) to "Route 114",
            (0 to 240) to "Route 115",
            (0 to 248) to "Route 116",
            (0 to 256) to "Route 117",
            (0 to 264) to "Route 118",
            (0 to 272) to "Route 119",
            (0 to 280) to "Route 120",
            (0 to 288) to "Route 121",
            (0 to 296) to "Route 122",
            (0 to 304) to "Route 123",
            (0 to 312) to "Route 124",
            (0 to 320) to "Route 125",
            (0 to 328) to "Route 126",
            (0 to 336) to "Route 127",
            (0 to 344) to "Route 128",
            (0 to 352) to "Route 129",
            (0 to 360) to "Route 130",
            (0 to 368) to "Route 131",
            (0 to 376) to "Route 132",
            (0 to 384) to "Route 133",
            (0 to 392) to "Route 134"
        ).withDefault { "Unknown Location" }

        fun get(group: Short, number: Short): String {
            return MAP.getValue((group.toInt() to number.toInt()))
        }

        fun map(): Map<Pair<Int, Int>, String> {
            return MAP.toMap()
        }

        fun routes(): Map<Pair<Int, Int>, String> {
            return MAP.toMap().filter { it.key.second >= 128 }
        }
    }
}
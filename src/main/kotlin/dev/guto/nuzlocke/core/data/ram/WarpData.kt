package dev.guto.nuzlocke.core.data.ram

import dev.guto.nuzlocke.core.data.static.Locations

data class WarpData(val mapGroup: Short, val mapNum: Short, val warpId: Short, val x: Int, val y: Int) {
    val locationName: String = Locations.get(mapGroup, mapNum)
}
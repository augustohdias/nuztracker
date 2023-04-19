package dev.guto.nuzlocke.util

class ByteArrayExtension {
    companion object {
        fun ByteArray.toUShortLE(offset: Int = 0): UShort {
            return this.toShortLE(offset).toUShort()
        }

        fun ByteArray.toUIntLE(offset: Int = 0): UInt {
            return this.toIntLE(offset).toUInt()
        }

        fun ByteArray.toShortLE(offset: Int = 0): Short {
            return (0 or (this[offset].toInt() and 0x000000FF shl 0) * 8).toShort()
        }

        fun ByteArray.toIntLE(offset: Int = 0): Int {
            var value = 0
            for (i in 0 until 3) {
                value = value or (this[i + offset].toInt() and 0x000000FF shl i) * 8
            }
            return value
        }
    }
}
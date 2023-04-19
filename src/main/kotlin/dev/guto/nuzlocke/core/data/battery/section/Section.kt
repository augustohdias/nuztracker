package dev.guto.nuzlocke.core.data.battery.section

import dev.guto.nuzlocke.core.data.battery.section.type.SectionId
import dev.guto.nuzlocke.util.ByteArrayExtension.Companion.toUIntLE
import dev.guto.nuzlocke.util.ByteArrayExtension.Companion.toUShortLE

data class Section(
    val data: ByteArray,
    val sectionId: SectionId,
    val checksum: UShort,
    val security: UInt,
    val counter: Int
) {

    companion object {
        private const val DATA_SIZE = 0xF80

        fun loadSection(bytes: ByteArray): Section {
            return Section(
                bytes.copyOfRange(
                    0,
                    DATA_SIZE
                ),
                SectionId.fromIndex(bytes.toUShortLE(0x0FF4)),
                bytes.toUShortLE(0x0FF6),
                bytes.toUIntLE(0x0FF8),
                (bytes.toUIntLE(0x0FFC).toInt() / 8)
            )
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Section

        if (!data.contentEquals(other.data)) return false
        if (sectionId != other.sectionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + sectionId.hashCode()
        return result
    }
}
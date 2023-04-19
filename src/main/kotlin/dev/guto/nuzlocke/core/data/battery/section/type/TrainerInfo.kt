package dev.guto.nuzlocke.core.data.battery.section.type

import dev.guto.nuzlocke.util.encoding.PokemonEncoding.Companion.decode
import dev.guto.nuzlocke.util.ByteArrayExtension.Companion.toUIntLE

data class TrainerInfo(
    val name: String,
    val gender: String,
    val trainerId: UInt,
    val timePlayed: ByteArray,
    val options: ByteArray,
    val securityKey: UInt
) {
    companion object {
        private const val NAME_SIZE = 7

        private const val GENDER_OFFSET = 0X0008

        private const val TRAINER_ID_OFFSET = 0x000A
        private const val TRAINER_ID_SIZE = 4

        private const val TIME_PLAYED_OFFSET = 0x000E
        private const val TIME_PLAYED_SIZE = 5

        private const val OPTIONS_OFFSET = 0x0013
        private const val OPTIONS_SIZE = 3

        private const val SECURITY_KEY_OFFSET = 0x00AC
        private const val SECURITY_KEY_SIZE = 4

        fun read(bytes: ByteArray): TrainerInfo {
            return TrainerInfo(
                bytes.copyOfRange(0, NAME_SIZE).joinToString("") { it.decode() },
                if (bytes[GENDER_OFFSET].toInt() == 1) {
                    "Female"
                } else {
                    "Male"
                },
                bytes.copyOfRange(TRAINER_ID_OFFSET, TRAINER_ID_OFFSET + TRAINER_ID_SIZE).toUIntLE(),
                bytes.copyOfRange(TIME_PLAYED_OFFSET, TIME_PLAYED_OFFSET + TIME_PLAYED_SIZE),
                bytes.copyOfRange(OPTIONS_OFFSET, OPTIONS_OFFSET + OPTIONS_SIZE),
                bytes.copyOfRange(SECURITY_KEY_OFFSET, SECURITY_KEY_OFFSET + SECURITY_KEY_SIZE).toUIntLE(),
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrainerInfo

        if (name != other.name) return false
        if (gender != other.gender) return false
        if (trainerId != other.trainerId) return false
        if (!timePlayed.contentEquals(other.timePlayed)) return false
        if (!options.contentEquals(other.options)) return false
        if (securityKey != other.securityKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + trainerId.hashCode()
        result = 31 * result + timePlayed.contentHashCode()
        result = 31 * result + options.contentHashCode()
        result = 31 * result + securityKey.hashCode()
        return result
    }
}

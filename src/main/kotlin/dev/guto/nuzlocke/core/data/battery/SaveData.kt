package dev.guto.nuzlocke.core.data.battery

import dev.guto.nuzlocke.core.data.battery.section.Section

data class SaveData(
    val gameStateA: List<Section> = mutableListOf<Section>(),
    val gameStateB: List<Section> = mutableListOf<Section>(),
    val activeGameState: List<Section> = mutableListOf<Section>()
) {
    companion object {
        private const val SECTION_SIZE = 0x1000
        private const val MAX_SECTIONS = 14
        const val SECURITY_VALUE = 0x8012025

        fun readBytes(bytes: ByteArray): SaveData {
            val aSections = mutableListOf<Section>()
            val bSections = mutableListOf<Section>()
            for (i in 0 until MAX_SECTIONS) {
                val offset = i * SECTION_SIZE
                aSections.add(Section.loadSection(bytes.copyOfRange(offset, offset + SECTION_SIZE)))
            }
            for (i in 0 until MAX_SECTIONS) {
                val offset = (i + MAX_SECTIONS) * SECTION_SIZE
                bSections.add(Section.loadSection(bytes.copyOfRange(offset, offset + SECTION_SIZE)))
            }
            return SaveData(
                gameStateA = aSections,
                gameStateB = bSections,
                activeGameState = if (aSections.first().counter > bSections.first().counter) {
                    aSections
                } else {
                    bSections
                }
            )
        }
    }
}
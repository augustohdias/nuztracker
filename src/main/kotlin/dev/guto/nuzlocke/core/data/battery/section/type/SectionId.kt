package dev.guto.nuzlocke.core.data.battery.section.type

enum class SectionId(val index: Int) {
    TrainerInfo(0),
    TeamAndItems(8),
    GameState(16),
    MiscData(24),
    RivalInfo(32),
    PCBufferA(40),
    PCBufferB(48),
    PCBufferC(56),
    PCBufferD(64),
    PCBufferE(72),
    PCBufferF(80),
    PCBufferG(88),
    PCBufferH(96),
    PCBufferI(104),
    Undefined(-1)
    ;

    companion object {
        fun fromIndex(index: UShort): SectionId {
            return SectionId.values().find { index.toInt() == it.index } ?: Undefined
        }
    }
}
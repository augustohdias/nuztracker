package dev.guto.nuzlocke.core.socket.messages

enum class Headers {
    NULL,
    PXC,
    PXS,
    LOC,
    NAM,
    RESET,
    SAV,
    ;

    companion object {
        fun parse(header: String): Headers {
            return Headers.values().filter { it.name == header }.getOrElse(0) { NULL }
        }
    }
}
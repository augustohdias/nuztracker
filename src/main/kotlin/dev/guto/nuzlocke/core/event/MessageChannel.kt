package dev.guto.nuzlocke.core.event

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class MessageChannel<T : Any> {
    @Volatile
    var eventStorage: Queue<T> = ConcurrentLinkedQueue()

    fun consume(): Optional<T> {
        return Optional.ofNullable(eventStorage.poll())
    }

    fun produce(value: T): T {
        eventStorage.add(value)
        return value
    }
}
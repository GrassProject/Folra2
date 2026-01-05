package com.github.grassproject.folra.util.event

import com.github.grassproject.folra.Folra
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import java.util.function.Consumer

fun Listener.register() {
    Bukkit.getPluginManager().registerEvents(this, Folra.INSTANCE)
}

fun Listener.unregister() {
    HandlerList.unregisterAll(this)
}

fun Event.call() {
    Bukkit.getServer().pluginManager.callEvent(this)
}

inline fun <reified T : Event> event(
    ignoredCancelled: Boolean = false,
    priority: EventPriority = EventPriority.NORMAL,
    callback: Consumer<T>
): Listener {
    val listener = object : Listener {}
    Bukkit.getPluginManager().registerEvent(
        T::class.java,
        listener,
        priority,
        { _, event ->
            if (event is T) {
                callback.accept(event)
            }
        },
        Folra.INSTANCE,
        ignoredCancelled
    )
    return listener
}

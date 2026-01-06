package com.github.grassproject.folra.api.nms

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.util.event.event
import com.github.grassproject.folra.util.version.MinecraftVersion
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object NMSManager {

    lateinit var HANDLER: NMSHandler
        private set

    fun init(plugin: FolraPlugin) {
        val version = MinecraftVersion.ofVersion(plugin)

        HANDLER = when (version) {
            MinecraftVersion.V_1_21_8 -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
            else -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
        }
    }

    fun setup() {
        event<PlayerJoinEvent> {
            HANDLER.registerPacketListener(it.player)
        }
        event<PlayerQuitEvent> {
            HANDLER.unregisterPacketListener(it.player)
        }
    }
}
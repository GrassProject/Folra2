package com.github.grassproject.folra

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.api.event.event
import com.github.grassproject.folra.api.nms.NMSHandler
import com.github.grassproject.folra.test.TestCommand
import com.github.grassproject.folra.util.version.MinecraftVersion
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Folra : FolraPlugin() {

    companion object {
        val INSTANCE: Folra
            get() {
                return FolraPlugin.INSTANCE as Folra
            }

        lateinit var NMS_HANDLER: NMSHandler
    }

    override fun load() {
        FolraPlugin.INSTANCE = this

        NMS_HANDLER = when (MinecraftVersion.ofVersion(this)) {
            MinecraftVersion.V_1_21_8 -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
            else -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
        }

    }

    override fun onEnable() {
        event<PlayerJoinEvent> {
            NMS_HANDLER.registerPacketListener(it.player)
        }
        event<PlayerQuitEvent> {
            NMS_HANDLER.unregisterPacketListener(it.player)
        }

        this.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(TestCommand.command().build())
        }
    }
}
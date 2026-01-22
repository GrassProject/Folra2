package com.github.grassproject.folra

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.api.nms.NMSHandler
import com.github.grassproject.folra.item1.ItemHandler
import com.github.grassproject.folra.test.TestCommand
import com.github.grassproject.folra.util.version.MinecraftVersion
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

class BukkitFolraPlugin : FolraPlugin() {

    val modules = listOf(
        ItemHandler, // InventoryManager
    )

    companion object {
        val INSTANCE: BukkitFolraPlugin
            get() {
                return FolraPlugin.INSTANCE as BukkitFolraPlugin
            }

        lateinit var NMS_HANDLER: NMSHandler
    }

    override fun load() {
        FolraPlugin.INSTANCE = this

        NMS_HANDLER = when (MinecraftVersion.ofVersion(this)) {
            MinecraftVersion.V_1_21_8 -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
            else -> com.github.grassproject.folra.nms.v1_21_8.NMSHandlerImpl
        }

        println()
        println("aaaa")
        println(NMS_HANDLER)
        println()

    }

    override fun onEnable() {
        saveDefaultConfig()

        for (module in modules) {
            module.register(this)
        }

//        event<PlayerJoinEvent> {
//            NMS_HANDLER.registerPacketListener(it.player)
//        }
//        event<PlayerQuitEvent> {
//            NMS_HANDLER.unregisterPacketListener(it.player)
//        }

        testPlugin()

    }

    override fun onDisable() {
        for (module in modules) {
            module.unregister(this)
        }
    }

    fun testPlugin() {
        this.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(TestCommand.command().build())
        }

        // PacketInvListener.init()
    }
}
package com.github.grassproject.folra

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.api.nms.NMSHandler
import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.ItemHandler
import com.github.grassproject.folra.registry.FolraRegistry
import com.github.grassproject.folra.registry.register
import com.github.grassproject.folra.test.TestCommand
import com.github.grassproject.folra.util.Logger
import com.github.grassproject.folra.util.version.MinecraftVersion
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.key.Key

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
        val keys = config.getKeys(false)
        Logger.debug("개수: ${keys.size}")

        FolraRegistry.ITEM.clear()
        for (key in keys) {
            val section = config.getConfigurationSection(key)
            if (section == null) {
                Logger.debug("'$key' is null")
                continue
            }
            val folraItem = FolraItem.loadFromYml(section)
            if (folraItem != null) {
                folraItem.register("folra", Key.key(key.lowercase()))
                Logger.debug("아이템 등록 성공: folra:${Key.key(key.lowercase())}")
            } else {
                Logger.debug("'$key' loadFromYml is null.")
            }
        }
    }
}
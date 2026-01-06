package com.github.grassproject.folra.inventory

import com.github.grassproject.folra.Folra
import com.github.grassproject.folra.module.FolraModules

object InventoryManager : FolraModules {

    override fun register(folra: Folra) {
//        event<PacketContainerCloseEvent> {
//            it.then = {
//                onCloseMenu(it.player, true)
//            }
//        }
//        event<PacketContainerOpenEvent> { event ->
//            if (shouldIgnore(event.containerId, event.player)) {
//                onCloseMenu(event.player, true)
//                return@event
//            }
//            val inventory = openedInventories[event.player] ?: return@event
//            val viewer = inventory.viewers[event.player.uniqueId] ?: return@event
//
//            event.then = {
//                updateInventoryContent(inventory, viewer)
//            }
//        }
    }

    override fun unregister(folra: Folra) {

    }
}
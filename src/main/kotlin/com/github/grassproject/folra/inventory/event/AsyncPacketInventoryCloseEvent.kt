package com.github.grassproject.folra.inventory.event

import com.github.grassproject.folra.api.event.FolraEvent
import com.github.grassproject.folra.inventory.PacketInventory
import org.bukkit.entity.Player

class AsyncPacketInventoryCloseEvent(
    val player: Player,
    val inventory: PacketInventory
): FolraEvent(true)
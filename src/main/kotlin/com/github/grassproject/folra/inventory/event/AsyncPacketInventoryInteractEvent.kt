package com.github.grassproject.folra.inventory.event

import com.github.grassproject.folra.api.event.FolraEvent
import com.github.grassproject.folra.inventory.ButtonType
import com.github.grassproject.folra.inventory.InventoryViewer
import com.github.grassproject.folra.inventory.PacketInventory
import org.bukkit.inventory.ItemStack

class AsyncPacketInventoryInteractEvent(
    val viewer: InventoryViewer,
    val inventory: PacketInventory,
    val slot: Int,
    val buttonType: ButtonType,
    val cursor: ItemStack?,
    val slots: Map<Int,ItemStack>
): FolraEvent(true)
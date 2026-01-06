package com.github.grassproject.folra.api.event.packet

import com.github.grassproject.folra.api.event.PacketEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PacketContainerContentEvent(
    val player: Player,
    val containerId: Int,
    val contents: MutableList<ItemStack>,
    var carriedItem: ItemStack
): PacketEvent()
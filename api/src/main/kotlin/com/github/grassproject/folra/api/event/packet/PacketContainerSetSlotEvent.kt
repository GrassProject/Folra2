package com.github.grassproject.folra.api.event.packet

import com.github.grassproject.folra.api.event.PacketEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PacketContainerSetSlotEvent(
    val player: Player,
    val containerId: Int,
    val slot: Int,
    var itemStack: ItemStack
): PacketEvent()
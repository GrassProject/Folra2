package com.github.grassproject.folra.api.event.packet

import com.github.grassproject.folra.api.event.PacketEvent
import org.bukkit.entity.Player

class PacketContainerOpenEvent(
    val player: Player,
    val containerId: Int
): PacketEvent()
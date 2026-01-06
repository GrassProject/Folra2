package com.github.grassproject.folra.api.event.packet

import com.github.grassproject.folra.api.event.CancellableFolraEvent
import org.bukkit.entity.Player

class PacketContainerCloseEvent(
    val player: Player
): CancellableFolraEvent(true) // PacketEvent()
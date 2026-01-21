package com.github.grassproject.folra.util

import com.github.grassproject.folra.Folra
import org.bukkit.entity.Player

fun Player.sendPacket(packet: Any, silent: Boolean = false) {
    Folra.NMS_HANDLER.sendPacket(packet, this)
}
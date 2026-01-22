package com.github.grassproject.folra.util

import com.github.grassproject.folra.BukkitFolraPlugin
import org.bukkit.entity.Player

fun Player.sendPacket(packet: Any, silent: Boolean = false) {
    BukkitFolraPlugin.NMS_HANDLER.sendPacket(packet, this)
}
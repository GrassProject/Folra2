package com.github.grassproject.folra.api.nms

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.MenuType

interface NMSHandler {

    fun registerPacketListener(player: Player)
    fun unregisterPacketListener(player: Player)

    fun openInventoryPacket(menuType: MenuType, title: Component): Any
    fun openInventory(player: Player, menuType: MenuType, title: Component)
}
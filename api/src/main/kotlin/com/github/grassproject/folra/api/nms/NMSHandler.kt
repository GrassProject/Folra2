package com.github.grassproject.folra.api.nms

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MenuType

interface NMSHandler {

    fun registerPacketListener(player: Player)
    fun unregisterPacketListener(player: Player)

//    fun setSlotItemPacket(containerId: Int, stateId: Int, slot: Int, itemStack: ItemStack?): Any
//    fun setSlotItem(containerId: Int, stateId: Int, slot: Int, itemStack: ItemStack, vararg players: Player)
//
//    fun setContainerItemsPacket(containerId: Int, stateId: Int, items: Collection<ItemStack?>, carriedItem: ItemStack?): Any
//    fun setContainerItems(containerId: Int, stateId: Int, items: Collection<ItemStack?>, carriedItem: ItemStack?, vararg players: Player)
//
//    fun openContainerPacket(containerId: Int, menuType: MenuType, title: Component): Any
//    fun openContainer(containerId: Int, menuType: MenuType, title: Component, vararg players: Player)
//
//    fun sendPacket(packet: Any, silent: Boolean = false, vararg players: Player)
//
//    fun receiveWindowClick(
//        inventoryId: Int,
//        stateId: Int,
//        slot: Int,
//        buttonNum: Int,
//        clickTypeNum: Int,
//        carriedItem: ItemStack?,
//        changedSlots: Map<Int, ItemStack?>,
//        vararg players: Player
//    )

}
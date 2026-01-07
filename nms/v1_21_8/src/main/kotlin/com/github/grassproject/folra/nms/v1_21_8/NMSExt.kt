package com.github.grassproject.folra.nms.v1_21_8

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.Component as NMSComponent
import net.minecraft.network.protocol.Packet
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack as NMSItemStack
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

inline val Player.handle: ServerPlayer
    get() = (this as CraftPlayer).handle

fun Player.sendPacket(packet: Packet<*>) {
    this.handle.connection.send(packet)
}

private fun ItemStack.toNMS(): NMSItemStack {
    return CraftItemStack.asNMSCopy(this)
}

private fun Component.toNMSComponent(): NMSComponent {
    return PaperAdventure.asVanilla(this)
}
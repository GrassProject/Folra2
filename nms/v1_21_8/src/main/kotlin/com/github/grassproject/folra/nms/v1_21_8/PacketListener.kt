package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.event.call
import com.github.grassproject.folra.api.event.packet.*
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.core.NonNullList
import net.minecraft.network.HashedStack
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import net.minecraft.world.item.ItemStack as NMSItemStack

class PacketListener(
    val player: Player,
) : ChannelDuplexHandler() {

//    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
//        super.write(ctx, if (msg is Packet<*>) msg.handleClientbound() else msg, promise)
//    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        super.channelRead(ctx, if (msg is Packet<*>) msg.handleServerbound() else msg)
    }

    private fun <T : ClientGamePacketListener> Packet<in T>.handleClientbound(): Packet<in T>? {
        when (this) {
            is ClientboundContainerSetSlotPacket -> {
                val event = PacketContainerSetSlotEvent(
                    player,
                    containerId,
                    stateId,
                    CraftItemStack.asCraftMirror(this.item)
                )
                event.call()
                if (event.isCancelled) {
                    return null
                }

                val newPack = ClientboundContainerSetSlotPacket(
                    containerId,
                    stateId,
                    slot,
                    CraftItemStack.asNMSCopy(event.itemStack)
                )
                return newPack
            }

            is ClientboundContainerSetContentPacket -> {
                val event = PacketContainerContentEvent(
                    player,
                    this.containerId,
                    this.items.map { (CraftItemStack.asCraftMirror(it)) }.toMutableList(),
                    CraftItemStack.asCraftMirror(this.carriedItem)
                )
                event.call()
                if (event.isCancelled) {
                    return null
                }

                val newPacket = ClientboundContainerSetContentPacket(
                    this.containerId,
                    this.stateId,
                    NonNullList.create<NMSItemStack>().apply {
                        addAll(event.contents.map { (CraftItemStack.asNMSCopy(it)) })
                    },
                    CraftItemStack.asNMSCopy(event.carriedItem)
                )
                return newPacket
            }

            is ClientboundOpenScreenPacket -> {
                val event = PacketContainerOpenEvent(player, this.containerId)
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return this
            }

            is ClientboundContainerClosePacket -> {
                val event = PacketContainerCloseEvent(player)
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return this
            }

        }
        return this
    }

    private fun <T : ClientGamePacketListener> Packet<in T>.handleServerbound(): Packet<in T>? {
        when (this) {
            is ServerboundContainerClosePacket -> {
                val event = PacketContainerCloseEvent(player)
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return this
            }

            is ServerboundContainerClickPacket -> {
                val carriedItem = (this.carriedItem as? HashedStack.ActualItem)?.let { carried ->
                    val type = carried.item.registeredName
                    carried.components.addedComponents

                    var item = NamespacedKey.fromString(type)?.let { typeKey ->
                        Registry.ITEM.get(typeKey)
                    }?.createItemStack(carried.count)

                    if (item != null) {
                        if (item.type == Material.AIR) return@let null
                        val nmsItem = CraftItemStack.asNMSCopy(item)
                        nmsItem.applyComponents(nmsItem.components)
                        item = CraftItemStack.asBukkitCopy(nmsItem)
                    }
                    item
                }
                val event = PacketContainerClickEvent(
                    player,
                    this.containerId,
                    this.stateId,
                    this.slotNum.toInt(),
                    this.buttonNum.toInt(),
                    this.clickType.ordinal,
                    carriedItem,
                    this.changedSlots.mapValues { null as ItemStack? },
                )
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return this
            }
        }
        return this
    }

}
package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.event.PacketEvent
import com.github.grassproject.folra.api.event.call
import com.github.grassproject.folra.api.event.packet.PacketContainerClickEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerCloseEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerContentEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerOpenEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerSetSlotEvent
import com.github.grassproject.folra.api.nms.ProtectedPacket
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.core.NonNullList
import net.minecraft.network.HashedStack
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PacketListener(
    val player: Player,
) : ChannelDuplexHandler() {

    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        if (msg is ProtectedPacket) {
            super.write(ctx, msg.packet, promise)
            return
        }

        val packets =
            if (msg is ClientboundBundlePacket) msg.subPackets() else listOf<Packet<in ClientGamePacketListener>>(
                msg as? Packet<ClientGamePacketListener> ?: return super.write(
                    ctx,
                    msg,
                    promise
                )
            )

        val newPackets = ArrayList<Packet<in ClientGamePacketListener>>()
        val thens = ArrayList<() -> Unit>()
        for (subPacket in packets) {
            val pair = handlePacket(subPacket) ?: continue
            val (resultPacket, resultEvent) = pair
            resultEvent?.let { thens.add(it.then) }
            newPackets.add(resultPacket)
        }
        if (newPackets.isEmpty()) {
            return
        }
        if (newPackets.size == 1) {
            super.write(ctx, newPackets[0], promise)
            thens.forEach { it() }
            return
        }
        super.write(ctx, ClientboundBundlePacket(newPackets), promise)
        thens.forEach { it() }
        return
    }

    fun handlePacket(packet: Packet<in ClientGamePacketListener>): Pair<Packet<in ClientGamePacketListener>, PacketEvent?>? {
        when (packet) {
            is ClientboundContainerSetSlotPacket -> {
                val event = PacketContainerSetSlotEvent(
                    player,
                    packet.containerId,
                    packet.stateId,
                    CraftItemStack.asCraftMirror(packet.item)
                )
                event.call()
                if (event.isCancelled) {
                    return null
                }
                val newPack = ClientboundContainerSetSlotPacket(
                    packet.containerId,
                    packet.stateId,
                    packet.slot,
                    CraftItemStack.asNMSCopy(event.itemStack)
                )
                return newPack to event
            }

            is ClientboundContainerSetContentPacket -> {
                val event = PacketContainerContentEvent(
                    player,
                    packet.containerId,
                    packet.items.map { (CraftItemStack.asCraftMirror(it)) }.toMutableList(),
                    CraftItemStack.asCraftMirror(packet.carriedItem)
                )
                event.call()
                if (event.isCancelled) {
                    return null
                }
                val newPacket = ClientboundContainerSetContentPacket(
                    packet.containerId,
                    packet.stateId,
                    NonNullList.create<net.minecraft.world.item.ItemStack>().apply {
                        addAll(event.contents.map { (CraftItemStack.asNMSCopy(it)) })
                    },
                    CraftItemStack.asNMSCopy(event.carriedItem)
                )
                return newPacket to event
            }

            is ClientboundOpenScreenPacket -> {
                val event = PacketContainerOpenEvent(player, packet.containerId)
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return packet to event
            }

            is ClientboundContainerClosePacket -> {
                val event = PacketContainerCloseEvent(player)
                event.call()
                if (event.isCancelled) {
                    return null
                }
                return packet to event
            }

        }
        return packet to null
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg is ProtectedPacket) {
            super.channelRead(ctx, msg.packet)
            return
        }

        when (msg) {
            is ServerboundContainerClosePacket -> {
                val event = PacketContainerCloseEvent(player)
                event.call()
                if (event.isCancelled) {
                    return
                }
                super.channelRead(ctx, msg)
                return
            }

            is ServerboundContainerClickPacket -> {
                val carriedItem = (msg.carriedItem as? HashedStack.ActualItem)?.let { carried ->
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
                    msg.containerId,
                    msg.stateId,
                    msg.slotNum.toInt(),
                    msg.buttonNum.toInt(),
                    msg.clickType.ordinal,
                    carriedItem,
                    msg.changedSlots.mapValues { null as ItemStack? },
                )
                event.call()
                if (event.isCancelled) {
                    return
                }
                super.channelRead(ctx, msg)
                return
            }
        }
        super.channelRead(ctx, msg)
    }
}
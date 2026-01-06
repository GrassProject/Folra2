package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.event.call
import com.github.grassproject.folra.api.event.packet.PacketContainerClickEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerCloseEvent
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.HashedStack
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

//    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
//
//    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
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
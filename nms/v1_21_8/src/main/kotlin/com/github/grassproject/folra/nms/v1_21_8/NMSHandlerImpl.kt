package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.nms.NMSHandler
import com.github.grassproject.folra.api.nms.PacketHandler
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.world.item.ItemStack as NMSItemStack
import net.minecraft.network.chat.Component as NMSComponent
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket
import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.inventory.CraftMenuType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MenuType

object NMSHandlerImpl : NMSHandler {

    override fun createPacketHandler(player: Player): PacketHandler {
        TODO("Not yet implemented")
    }

//    override fun registerPacketListener(player: Player) {
//        val channel = player.serverPlayer.connection.connection.channel ?: return
//        channel.eventLoop().execute {
//            try {
//                val pipeline = channel.pipeline()
//                if (pipeline.get("folra_packet_listener") == null) {
//                    pipeline.addBefore("packet_handler", "folra_packet_listener", PacketListener(player))
//                }
//            } catch (_: Exception) {}
//        }
//    }
//        val connection = player.serverPlayer.connection.connection
//        val pipeline = connection.channel.pipeline()
//
//        if (pipeline.get("folra_packet_listener") == null) {
//            pipeline.addBefore("packet_handler", "folra_packet_listener", PacketListener(player))
//        }

//    override fun unregisterPacketListener(player: Player) {
//        val connection = player.serverPlayer.connection.connection
//        val channel = connection.channel
//        val pipeline = channel.pipeline()
//        if (channel != null) {
//            try {
//                if (pipeline.names().contains("folra_packet_listener")) {
//                    pipeline.remove("folra_packet_listener")
//                }
//            } catch (_: Exception) {
//            }
//        }
//    }

//    override fun unregisterPacketListener(player: Player) {
//        val channel = player.serverPlayer.connection.connection.channel ?: return
//        channel.eventLoop().execute {
//            try {
//                val pipeline = channel.pipeline()
//                if (pipeline.context("folra_packet_listener") != null) {
//                    pipeline.remove("folra_packet_listener")
//                }
//            } catch (_: Exception) {}
//        }
//    }

    override fun setSlotItemPacket(
        containerId: Int,
        stateId: Int,
        slot: Int,
        itemStack: ItemStack
    ): Any {
        val packet = ClientboundContainerSetSlotPacket(
            containerId,
            stateId,
            slot,
            CraftItemStack.asNMSCopy(itemStack)
        )
        return packet
    }

    override fun setSlotItem(
        containerId: Int,
        stateId: Int,
        slot: Int,
        itemStack: ItemStack,
        vararg players: Player
    ) {
        val packet = setSlotItemPacket(containerId, stateId, slot, itemStack) as Packet<*>
        for (player in players) {
            player.sendPacket(packet)
        }
    }

    override fun setContainerItemsPacket(
        containerId: Int,
        stateId: Int,
        items: Collection<ItemStack?>,
        carriedItem: ItemStack?
    ): Any {
        val nmsItems = NonNullList.create<NMSItemStack>()
        nmsItems += items.map { it?.toNMS() ?: NMSItemStack.EMPTY }
        val packet = ClientboundContainerSetContentPacket(
            containerId,
            stateId,
            nmsItems,
            carriedItem?.toNMS() ?: NMSItemStack.EMPTY
        )
        return packet
    }

    override fun setContainerItems(
        containerId: Int,
        stateId: Int,
        items: Collection<ItemStack?>,
        carriedItem: ItemStack?,
        vararg players: Player
    ) {
        val packet = setContainerItemsPacket(containerId, stateId, items, carriedItem) as Packet<*>
        for (player in players) {
            player.sendPacket(packet)
        }
    }

    override fun openContainerPacket(
        containerId: Int,
        menuType: MenuType,
        title: Component
    ): Any {
        val packet = ClientboundOpenScreenPacket(
            containerId,
            CraftMenuType.bukkitToMinecraft(menuType),
            title.toNMSComponent()
        )
        return packet
    }

    override fun openContainer(
        containerId: Int,
        menuType: MenuType,
        title: Component,
        vararg players: Player
    ) {
        val packet = openContainerPacket(containerId, menuType, title) as Packet<*>
        for (player in players) {
            player.sendPacket(packet)
        }
    }

    inline val Player.craftPlayer: CraftPlayer
        get() = this as CraftPlayer

    inline val Player.serverPlayer: ServerPlayer
        get() = (this as CraftPlayer).handle

    fun Player.sendPacket(packet: Packet<*>) {
        this.serverPlayer.connection.send(packet)
    }

    private fun ItemStack.toNMS(): NMSItemStack {
        return CraftItemStack.asNMSCopy(this)
    }

    private fun Component.toNMSComponent(): NMSComponent {
        val kyoriJson = GsonComponentSerializer.gson().serialize(this)
        return ComponentSerialization.CODEC.parse(
            JsonOps.INSTANCE,
            JsonParser.parseString(kyoriJson)
        ).orThrow
        // return PaperAdventure.asVanilla(this)
    }
}
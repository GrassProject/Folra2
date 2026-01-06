package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.nms.CommonMenuManager
import com.github.grassproject.folra.api.nms.NMSHandler
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftMenuType
import org.bukkit.entity.Player
import org.bukkit.inventory.MenuType

object NMSHandlerImpl : NMSHandler {
    override fun registerPacketListener(player: Player) {
        val packetListener = PacketListener(player)
        val connection = player.serverPlayer.connection.connection
        val pipeline = connection.channel.pipeline()

        pipeline.addBefore("packet_handler", "folra_packet_listener", packetListener)
    }

    override fun unregisterPacketListener(player: Player) {
        val connection = player.serverPlayer.connection.connection
        val channel = connection.channel
        val pipeline = channel.pipeline()
        if (channel != null) {
            try {
                if (pipeline.names().contains("folra_packet_listener")) {
                    pipeline.remove("folra_packet_listener")
                }
            } catch (_: Exception) {
            }
        }
    }

    override fun openInventoryPacket(
        menuType: MenuType,
        title: Component
    ): Any {
        val packet = ClientboundOpenScreenPacket(
            CommonMenuManager.INVENTORY_ID,
            CraftMenuType.bukkitToMinecraft(menuType),
            title.toNMSComponent()
        )
        return packet
    }

    override fun openInventory(
        player: Player,
        menuType: MenuType,
        title: Component
    ) {
        val packet = openInventoryPacket(menuType, title) as Packet<*>
        player.sendPacket(packet)
    }

    val Player.serverPlayer: ServerPlayer
        get() = craftPlayer.handle
    val Player.craftPlayer: CraftPlayer
        get() = (this as CraftPlayer)

    private fun Player.sendPacket(packet: Packet<*>) {
        craftPlayer.handle.connection.send(packet)
    }

    private fun Component.toNMSComponent(): net.minecraft.network.chat.Component {
        val kyoriJson = GsonComponentSerializer.gson().serialize(this)
        return ComponentSerialization.CODEC.parse(
            JsonOps.INSTANCE,
            JsonParser.parseString(kyoriJson)
        ).orThrow
    }
}
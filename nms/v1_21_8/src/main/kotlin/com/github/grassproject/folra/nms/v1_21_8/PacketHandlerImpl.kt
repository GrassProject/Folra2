package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.nms.PacketHandler
import io.netty.channel.ChannelDuplexHandler
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponents
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.ItemStack as NMSItemStack
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.function.BiFunction

class PacketHandlerImpl(
    private val player: Player
) : PacketHandler, ChannelDuplexHandler() {

    companion object {
        private const val IDENTIFIER = "folra_packet_listener"
    }

    private val connection = player.handle.connection

    init {
        val pipeline = connection.connection.channel.pipeline()
        pipeline.context(Connection::class.java)?.let {
            pipeline.addBefore("packet_handler", IDENTIFIER, this)
        }
    }

    override fun close() {
        val channel = connection.connection.channel
        channel.eventLoop().submit {
            channel.pipeline().remove(IDENTIFIER)
        }
    }

    private fun <T : ClientGamePacketListener> Packet<in T>.handleClientbound(): Packet<in T> {
        fun mapClientsideLore(itemStack: NMSItemStack, function: BiFunction<ItemStack, Player, List<Component>>?) {
            function ?: return
            val lore = function.apply(itemStack.bukkitStack, player)
            itemStack.set(DataComponents.LORE, ItemLore(lore.map { PaperAdventure.asVanilla(it) }))
        }

        when (this) {

        }
    }

    val Player.handle: ServerPlayer
        get() = (this as CraftPlayer).handle

}
package com.github.grassproject.folra.nms.v1_21_8

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.api.nms.NMSHandler
import com.github.grassproject.folra.api.nms.PacketHandler
import com.google.common.hash.HashCode
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import io.papermc.paper.adventure.PaperAdventure
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.world.item.ItemStack as NMSItemStack
import net.minecraft.network.chat.Component as NMSComponent
import net.minecraft.core.NonNullList
import net.minecraft.core.component.TypedDataComponent
import net.minecraft.network.Connection
import net.minecraft.network.HashedPatchMap
import net.minecraft.network.HashedStack
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.HashOps
import net.minecraft.world.inventory.ClickType
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.inventory.CraftMenuType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MenuType

object NMSHandlerImpl : NMSHandler {

    override fun registerPacketListener(player: Player) {
        val channel = player.handle.connection.connection.channel ?: return
        channel.eventLoop().execute {
            try {
                val pipeline = channel.pipeline()
                if (pipeline.get("folra_packet_listener") == null) {
                    pipeline.addBefore("packet_handler", "folra_packet_listener", PacketListener(player))
                }
            } catch (_: Exception) {}
        }
    }

    override fun unregisterPacketListener(player: Player) {
        val connection = player.handle.connection.connection
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

//    override fun setSlotItemPacket(
//        containerId: Int,
//        stateId: Int,
//        slot: Int,
//        itemStack: ItemStack?
//    ): Any {
//        val packet = ClientboundContainerSetSlotPacket(
//            containerId,
//            stateId,
//            slot,
//            CraftItemStack.asNMSCopy(itemStack)
//        )
//        return packet
//    }
//
//    override fun setSlotItem(
//        containerId: Int,
//        stateId: Int,
//        slot: Int,
//        itemStack: ItemStack,
//        vararg players: Player
//    ) {
//        val packet = setSlotItemPacket(containerId, stateId, slot, itemStack) as Packet<*>
//        for (player in players) {
//            player.sendPacket(packet)
//        }
//    }
//
//    override fun setContainerItemsPacket(
//        containerId: Int,
//        stateId: Int,
//        items: Collection<ItemStack?>,
//        carriedItem: ItemStack?
//    ): Any {
//        val nmsItems = NonNullList.create<NMSItemStack>()
//        nmsItems += items.map { it?.toNMS() ?: NMSItemStack.EMPTY }
//        val packet = ClientboundContainerSetContentPacket(
//            containerId,
//            stateId,
//            nmsItems,
//            carriedItem?.toNMS() ?: NMSItemStack.EMPTY
//        )
//        return packet
//    }
//
//    override fun setContainerItems(
//        containerId: Int,
//        stateId: Int,
//        items: Collection<ItemStack?>,
//        carriedItem: ItemStack?,
//        vararg players: Player
//    ) {
//        val packet = setContainerItemsPacket(containerId, stateId, items, carriedItem) as Packet<*>
//        for (player in players) {
//            player.sendPacket(packet)
//        }
//    }
//
//    override fun openContainerPacket(
//        containerId: Int,
//        menuType: MenuType,
//        title: Component
//    ): Any {
//        val packet = ClientboundOpenScreenPacket(
//            containerId,
//            CraftMenuType.bukkitToMinecraft(menuType),
//            title.toNMSComponent()
//        )
//        return packet
//    }
//
//    override fun openContainer(
//        containerId: Int,
//        menuType: MenuType,
//        title: Component,
//        vararg players: Player
//    ) {
//        val packet = openContainerPacket(containerId, menuType, title) as Packet<*>
//        for (player in players) {
//            player.sendPacket(packet)
//        }
//    }
//
//    override fun sendPacket(packet: Any, silent: Boolean, vararg players: Player) {
//        if (packet !is Packet<*>) return
//        for (player in players) {
//            if (silent) {
//                val networkManager = player.handle.connection.connection
//                networkManager.channel.writeAndFlush(packet, networkManager.channel.voidPromise())
//            } else {
//                player.sendPacket(packet)
//            }
//        }
//    }
//
//    override fun receiveWindowClick(
//        inventoryId: Int,
//        stateId: Int,
//        slot: Int,
//        buttonNum: Int,
//        clickTypeNum: Int,
//        carriedItem: ItemStack?,
//        changedSlots: Map<Int, ItemStack?>,
//        vararg players: Player,
//    ) {
//        val registryAccess = (Bukkit.getWorlds().first() as CraftWorld).handle.registryAccess()
//        val registryOps: RegistryOps<HashCode> = registryAccess.createSerializationContext(HashOps.CRC32C_INSTANCE);
//        val hashOpsGenerator: HashedPatchMap.HashGenerator = HashedPatchMap.HashGenerator { typedDataComponent ->
//            typedDataComponent.encodeValue(registryOps).getOrThrow { string ->
//                IllegalArgumentException("Failed to hash $typedDataComponent: $string")
//            }.asInt()
//        }
//
//        val map = Int2ObjectOpenHashMap<HashedStack>()
//        changedSlots.forEach { (key, value) ->
//            val nmsItem = value?.toNMS() ?: NMSItemStack.EMPTY
//            map[key] = HashedStack.create(nmsItem, hashOpsGenerator)
//        }
//
//        val packet = ServerboundContainerClickPacket(
//            inventoryId,
//            stateId,
//            slot.toShort(),
//            buttonNum.toByte(),
//            ClickType.entries[clickTypeNum],
//            map,
//            HashedStack.create(carriedItem?.toNMS() ?: NMSItemStack.EMPTY, hashOpsGenerator)
//        )
//
//        Bukkit.getScheduler().runTask(FolraPlugin.INSTANCE, Runnable {
//            for (player in players) {
//                (player as CraftPlayer).handle.connection.handleContainerClick(packet)
//            }
//        })
//    }

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
}
package com.github.grassproject.folra.test

import com.github.grassproject.folra.Folra
import com.github.grassproject.folra.inventory.InventoryManager
import com.github.grassproject.folra.inventory.InventoryType
import com.github.grassproject.folra.inventory.PacketInventory
import com.github.grassproject.folra.util.toMMComponent
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object TestCommand {

    fun command(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("packet_inv")
            .executes { context ->
                val sender = context.source.executor

                if (sender is Player) {
                    // Folra.NMS_HANDLER.openContainer(CommonMenuManager.CONTAINER_ID, InventoryType.HOPPER.menuType, "aa".toMMComponent(), sender)
                    InventoryManager.openMenu(
                        sender,
                        PacketInventory(
                            "<green>Packet Hopper".toMMComponent(),
                            InventoryType.HOPPER
                        ).apply {
                            setItem(2, ItemStack(Material.HOPPER))
                        }
                    )
                }

                Command.SINGLE_SUCCESS
            }
    }
}
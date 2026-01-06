package com.github.grassproject.folra.test

import com.github.grassproject.folra.Folra
import com.github.grassproject.folra.inventory.InventoryType
import com.github.grassproject.folra.util.toMMComponent
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

object TestCommand {

    fun command(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("plant")
            .executes { context ->
                val sender = context.source.executor

                if (sender is Player) {
                    Folra.NMS_HANDLER.openInventory(sender, InventoryType.HOPPER.menuType, "aa".toMMComponent())
                }

                Command.SINGLE_SUCCESS
            }
    }
}
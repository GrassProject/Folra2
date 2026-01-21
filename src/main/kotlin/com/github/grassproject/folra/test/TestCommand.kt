package com.github.grassproject.folra.test

import com.github.grassproject.folra.item.folraItem
import com.github.grassproject.folra.util.toMMComponent
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

object TestCommand {

    fun command(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("packet_inv")
            .executes { context ->
                val sender = context.source.executor

                if (sender is Player) {
                    val folraItem = folraItem(Material.STONE) {
                        displayName = "Test".toMMComponent()
                        lore(
                            "aa".toMMComponent(),
                            "bbbb".toMMComponent()
                        )
                        enchants["sharpness"] = 10
                        flag(ItemFlag.HIDE_ATTRIBUTES)
                    }
                    folraItem.giveItem(sender, 5)
                }

                Command.SINGLE_SUCCESS
            }
    }
}
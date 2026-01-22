package com.github.grassproject.folra.test

import com.github.grassproject.folra.BukkitFolraPlugin
import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.ItemHandler
import com.github.grassproject.folra.item.folraItem
import com.github.grassproject.folra.registry.FolraRegistry
import com.github.grassproject.folra.registry.register
import com.github.grassproject.folra.registry.registryId
import com.github.grassproject.folra.util.Logger
import com.github.grassproject.folra.util.toMMComponent
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType

object TestCommand {

    fun command(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("give_item")
            .executes { context ->
                val sender = context.source.executor

                if (sender is Player) {
                    val folraItem = folraItem(Material.FISHING_ROD) { /*folraItem("craftengine:default:globe") {*/
                        displayName = "<green>Test".toMMComponent()
                        lore(
                            "로어테스트 1",
                            "로어테스트 2",
                            "로어테스트 3"
                        )
                        lore.add("로어 테스트 4".toMMComponent())
                        enchants {
                            enchants["luck_of_the_sea"] = 5
                            Enchantment.POWER(10)
                            Enchantment.LURE level 10
                        }
                        customModelData {
                            floats.add(1.5f)
                            colors.add(Color.RED)
                        }
                        flags(
                            ItemFlag.HIDE_DYE
                        )
                    }
                    folraItem.giveItem(sender)

                    FolraRegistry.ITEM["folra:customfishing:bone_rod"]?.let {
                        sendLog(it)
                        it.giveItem(sender)
                    }

                    println("a")

                    FolraRegistry.ITEM["folra:minecraft:bone_rod"]?.let {
                        sendLog(it)
                        it.giveItem(sender)
                    }

                }

                Command.SINGLE_SUCCESS
            }
    }

    fun sendLog(folraItem: FolraItem) {
        println("PDC: ${folraItem.getUnmodifiedItem().persistentDataContainer.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING)}")
        println("real Id: ${folraItem.registryId()}")
    }
}
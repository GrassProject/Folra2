package com.github.grassproject.folra.test

import com.github.grassproject.folra.BukkitFolraPlugin
import com.github.grassproject.folra.item1.FolraItem
import com.github.grassproject.folra.item1.folraItem
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
                    callBoneRodItem()

                    FolraRegistry.ITEM["folra:customfishing:bone_rod"]?.let {
                        sendLog(it)
                        it.giveItem(sender)
                    }

                    FolraRegistry.ITEM["folra:bone_rod"]?.let {
                        sendLog(it)
                        it.giveItem(sender)
                    }

                }

                Command.SINGLE_SUCCESS
            }
    }

    fun callBoneRodItem() {
        val config = BukkitFolraPlugin.INSTANCE.config
        val keys = config.getKeys(false)

        Logger.debug("개수: ${keys.size}")

        FolraRegistry.ITEM.clear()
        for (key in keys) {
            val section = config.getConfigurationSection(key)
            if (section == null) {
                Logger.debug("'$key' is null")
                continue
            }
            val folraItem = FolraItem.loadFromYml(section)

            if (folraItem != null) {
                folraItem.register("folra",key.lowercase())
                Logger.debug("아이템 등록 성공: folra:${key.lowercase()}")
            } else {
                Logger.debug("'$key' loadFromYml is null.")
            }
        }
    }

    fun sendLog(folraItem: FolraItem) {
        println("factory Id: ${folraItem.factoryId}")
        println("internal Id: ${folraItem.internalId}")
        println("real Id: ${folraItem.registryId()}")
    }
}
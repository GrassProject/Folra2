package com.github.grassproject.folra.item1

import com.github.grassproject.folra.registry.serializer.ItemSerializer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface FolraItem {
    val factoryId: String?
    val internalId: String?

    fun getItem(): ItemStack
    fun getUnmodifiedItem(): ItemStack
    fun giveItem(player: Player, amount: Int = 1)

    companion object {
        fun loadFromYml(section: ConfigurationSection?): FolraItem? {
            return ItemSerializer.fromSection(section)
        }
    }
}
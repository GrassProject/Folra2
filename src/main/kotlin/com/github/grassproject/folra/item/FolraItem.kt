package com.github.grassproject.folra.item

import com.github.grassproject.folra.registry.serializer.ItemSerializer
import com.google.common.collect.HashMultimap
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class FolraItem(
    val factoryId: String?,
    val internalId: String? = null,
    private val item: ItemStack,
) {

    fun giveItem(player: Player) {
        val iS = getItem()
        player.inventory.addItem(iS)
    }

    fun giveItem(player: Player, amount: Int) {
        val iS = getItem()
        iS.amount = amount

        player.inventory.addItem(iS)
    }

    fun getUnmodifiedItem(): ItemStack {
        return item.clone()
    }

    fun getItem(): ItemStack {
        val iS = getUnmodifiedItem()

        val im = iS.itemMeta ?: return iS
        val modifiers = im.attributeModifiers
        if (modifiers == null) {
            im.attributeModifiers = HashMultimap.create(iS.type.defaultAttributeModifiers)
        }

        iS.itemMeta = im

        return iS
    }

    companion object {
        fun loadFromYml(section: ConfigurationSection?): FolraItem? {
            return ItemSerializer.fromSection(section)
        }

    }
}
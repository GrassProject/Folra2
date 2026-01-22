package com.github.grassproject.folra.item

import com.github.grassproject.folra.item.component.ItemComponentHandle
import com.github.grassproject.folra.item.component.ItemComponents
import com.github.grassproject.folra.registry.serializer.ItemSerializer
import com.google.common.collect.HashMultimap
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class FolraItem(
    private val item: ItemStack,
    components: Collection<ItemComponentHandle>,
) {

    private val components = components.associateBy { it.key }.toMutableMap()

    fun getComponent(key: Key): ItemComponentHandle? {
        return components[key]
    }

    fun getComponent(itemComponents: ItemComponents): ItemComponentHandle? {
        return getComponent(itemComponents.key)
    }

    fun giveItem(player: Player, amount: Int = 1) {
        val itemStack = getItem().apply {
            this.amount = amount
        }
        player.inventory.addItem(itemStack)
    }

    fun getUnmodifiedItem(): ItemStack = item.clone()

    fun getItem(): ItemStack {
        val itemStack = getUnmodifiedItem()

        val meta = itemStack.itemMeta ?: return itemStack
        val modifiers = meta.attributeModifiers
        if (modifiers == null) {
            meta.attributeModifiers = HashMultimap.create(itemStack.type.defaultAttributeModifiers)
        }

        components.values.forEach { it.apply(meta) }
        itemStack.itemMeta = meta

        components.values.forEach { it.apply(itemStack) }
        return itemStack
    }

    companion object {
        fun loadFromYml(section: ConfigurationSection?): FolraItem? {
            return ItemSerializer.fromSection(section)
        }
    }

}
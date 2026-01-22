package com.github.grassproject.folra.item1

import com.github.grassproject.folra.item1.component.ItemComponentHandle
import com.github.grassproject.folra.item1.component.ItemComponents
import com.google.common.collect.HashMultimap
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class FolraItemImpl(
    override val factoryId: String?,
    override val internalId: String? = null,
    private val item: ItemStack,
    itemComponents: Collection<ItemComponentHandle>
) : FolraItem {

    private val itemComponentsMap = itemComponents.associateBy { it.key }.toMutableMap()

    fun getComponent(key: Key): ItemComponentHandle? {
        return itemComponentsMap[key]
    }

    fun getComponent(itemComponents: ItemComponents): ItemComponentHandle? {
        return getComponent(itemComponents.key)
    }

    override fun giveItem(player: Player, amount: Int) {
        val itemStack = getItem().apply { this.amount = amount }
        player.inventory.addItem(itemStack)
    }

    override fun getUnmodifiedItem(): ItemStack {
        return item.clone()
    }

    override fun getItem(): ItemStack {
        val itemStack = getUnmodifiedItem()

        val itemMeta = itemStack.itemMeta ?: return itemStack
        if (itemMeta.attributeModifiers == null) {
            itemMeta.attributeModifiers = HashMultimap.create(itemStack.type.defaultAttributeModifiers)
        }

        itemComponentsMap.values.forEach { it.apply(itemMeta) }
        itemStack.itemMeta = itemMeta

        itemComponentsMap.values.forEach { it.apply(itemStack) }
        return itemStack
    }
}
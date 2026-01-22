package com.github.grassproject.folra.item1.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class ItemModelComponent(
    val itemModel: Key
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.ITEM_MODEL, itemModel)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:item-model")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val itemModel = section.getString("item-model")
                ?.let { runCatching { Key.key(it) }.getOrNull() }
                ?: return null
            return ItemModelComponent(itemModel)
        }

    }
}
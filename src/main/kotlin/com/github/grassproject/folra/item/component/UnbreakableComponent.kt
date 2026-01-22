package com.github.grassproject.folra.item.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class UnbreakableComponent : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.UNBREAKABLE)
    }

    companion object : ItemComponentLoader {
        override val key = Key.key("itemcomponent:unbreakable")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            section.getBoolean("unbreakable", false).takeIf { it } ?: return null
            return UnbreakableComponent()
        }

    }
}
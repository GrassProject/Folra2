package com.github.grassproject.folra.item.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class MaxStackSizeComponent(
    val maxStackSize: Int
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, maxStackSize)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:max-stack-size")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            if (!section.contains("max-stack-size")) return null
            return MaxStackSizeComponent(section.getInt("max-stack-size"))
        }
    }
}
package com.github.grassproject.folra.item.component

import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class AmountComponent(
    val amount: Int
): ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.amount = amount
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:amount")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            if (!section.contains("amount")) return null
            return AmountComponent(section.getInt("amount"))
        }

    }
}
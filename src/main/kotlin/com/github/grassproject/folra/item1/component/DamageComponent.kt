package com.github.grassproject.folra.item1.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class DamageComponent(
    val damage: Int
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.DAMAGE, damage)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:damage")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            if (!section.contains("damage")) return null
            return DamageComponent(section.getInt("damage"))
        }
    }
}
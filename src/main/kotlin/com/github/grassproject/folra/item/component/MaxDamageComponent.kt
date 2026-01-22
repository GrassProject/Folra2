package com.github.grassproject.folra.item.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class MaxDamageComponent(
    val maxDamage: Int
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.MAX_DAMAGE, maxDamage)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:max-damage")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            if (!section.contains("max-damage")) return null
            return MaxDamageComponent(section.getInt("max-damage"))
        }
    }
}
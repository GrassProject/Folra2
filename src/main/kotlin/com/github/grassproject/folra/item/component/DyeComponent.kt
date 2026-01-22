package com.github.grassproject.folra.item.component

import com.github.grassproject.folra.util.toColorOrNull
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import net.kyori.adventure.key.Key
import org.bukkit.Color
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class DyeComponent(
    val color: Color
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color))
    }

    companion object : ItemComponentLoader {
        override val key = Key.key("itemcomponent:dye")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val color = section.getString("dye")?.toColorOrNull() ?: return null
            return DyeComponent(color)
        }
    }
}
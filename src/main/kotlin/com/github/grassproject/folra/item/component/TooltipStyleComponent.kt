package com.github.grassproject.folra.item.component

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class TooltipStyleComponent(
    val tooltipStyle: Key
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.TOOLTIP_STYLE, tooltipStyle)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:tooltip-style")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val tooltipStyle = section.getString("tooltip-style")
                ?.let { runCatching { Key.key(it) }.getOrNull() }
                ?: return null
            return TooltipStyleComponent(tooltipStyle)
        }
    }
}
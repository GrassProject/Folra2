package com.github.grassproject.folra.item.component

import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

class FlagsComponent(
    val flags: List<ItemFlag>,
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemMeta: ItemMeta) {
        itemMeta.addItemFlags(*flags.toTypedArray())
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:flags")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val flags = section.getStringList("flags")
                .mapNotNull { runCatching { ItemFlag.valueOf(it.uppercase()) }.getOrNull() }
                .takeIf { it.isNotEmpty() } ?: return null
            return FlagsComponent(flags)
        }
    }
}
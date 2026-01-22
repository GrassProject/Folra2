package com.github.grassproject.folra.item1.component

import com.github.grassproject.folra.util.toMMComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.meta.ItemMeta

class DisplayNameComponent(
    displayName: Component
) : ItemComponentHandle {

    override val key: Key = Companion.key

    private val displayName = displayName.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    override fun apply(itemMeta: ItemMeta) {
        itemMeta.displayName(displayName)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:display-name")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val displayName = section.getString("display-name") ?: return null
            return DisplayNameComponent(displayName.toMMComponent())
        }

    }
}
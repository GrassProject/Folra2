package com.github.grassproject.folra.item1.component

import com.github.grassproject.folra.util.toMMComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class LoreComponent(
    lore: List<Component>
) : ItemComponentHandle {

    override val key: Key = Companion.key

    private val lore = lore.map { it.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
    override fun apply(itemStack: ItemStack) {
        itemStack.lore(lore)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:lore")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val lore = section.getStringList("lore")
                .map { it.toMMComponent() }
                .takeIf { it.isNotEmpty() } ?: return null
            return LoreComponent(lore)
        }
    }
}
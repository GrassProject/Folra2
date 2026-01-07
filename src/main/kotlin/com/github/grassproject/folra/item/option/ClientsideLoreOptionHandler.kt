package com.github.grassproject.folra.item.option

import com.github.grassproject.folra.util.toMMComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.meta.ItemMeta

class ClientsideLoreOptionHandler(
    val lore: List<Component>,
) : ItemOptionHandle {

    override val key = Companion.key
    override fun apply(itemMeta: ItemMeta) {
        itemMeta.lore(lore)
    }

    companion object : ItemOption {
        override val key = Key.key("itemoption:client-lore")
        override fun load(section: ConfigurationSection): ItemOptionHandle? {
            if (!section.contains("client-lore")) return null
            val lore = section.getStringList("client-lore").map {
                it.toMMComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            if (lore.isEmpty()) return null
            return ClientsideLoreOptionHandler(lore)
        }
    }

}
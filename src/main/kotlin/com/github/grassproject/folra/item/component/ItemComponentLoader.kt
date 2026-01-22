package com.github.grassproject.folra.item.component

import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection

interface ItemComponentLoader {
    val key: Key
    fun load(section: ConfigurationSection): ItemComponentHandle?
}
package com.github.grassproject.folra.util

import org.bukkit.Bukkit

object Logger {
    fun debug(string: String) {
        Bukkit.getConsoleSender().sendMessage("<gray>$string".toMMComponent())
    }
}
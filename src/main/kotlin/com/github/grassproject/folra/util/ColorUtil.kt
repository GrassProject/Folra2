package com.github.grassproject.folra.util

import org.bukkit.Color

object ColorUtil {
    fun parse(input: String?): Color? {
        if (input.isNullOrBlank()) return null
        val trimmed = input.trim()

        return runCatching {
            when {
                trimmed.startsWith("#") -> {
                    Color.fromRGB(trimmed.substring(1).toInt(16))
                }

                else -> {
                    val parts = trimmed.split(Regex("[;,\\s]+"))
                    if (parts.size == 3) {
                        Color.fromRGB(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                    } else null
                }
            }
        }.getOrNull()
    }
}

fun String?.toColorOrNull(): Color? = ColorUtil.parse(this)
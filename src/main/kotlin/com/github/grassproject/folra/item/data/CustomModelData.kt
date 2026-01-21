package com.github.grassproject.folra.item.data

import com.github.grassproject.folra.annotation.FolraDsl
import org.bukkit.Color

data class CustomModelData(
    val colors: List<Color>,
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>
) {
    @FolraDsl
    class Builder {
        val colors = mutableListOf<Color>()
        val floats = mutableListOf<Float>()
        val flags = mutableListOf<Boolean>()
        val strings = mutableListOf<String>()

        fun build() = CustomModelData(
            colors.toList(),
            floats.toList(),
            flags.toList(),
            strings.toList()
        )
    }
}
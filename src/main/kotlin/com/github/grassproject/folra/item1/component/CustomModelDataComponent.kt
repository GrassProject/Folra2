package com.github.grassproject.folra.item1.component

import com.github.grassproject.folra.util.toColorOrNull
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import net.kyori.adventure.key.Key
import org.bukkit.Color
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class CustomModelDataComponent(
    val colors: List<Color>,
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        itemStack.setData(
            DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addColors(colors)
                .addFloats(floats)
                .addFlags(flags)
                .addStrings(strings)
        )
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:cutom-model-data")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val section = section.getConfigurationSection("custom-model-data") ?: return null

            val colors = section.getStringList("colors").mapNotNull { it.toColorOrNull() }
            val floats = section.getStringList("floats").mapNotNull { it.toFloatOrNull() }
            val flags = section.getStringList("flags").map { it.toBoolean() }
            val strings = section.getStringList("strings")

            if (colors.isEmpty() && floats.isEmpty() && flags.isEmpty() && strings.isEmpty()) return null
            return CustomModelDataComponent(colors, floats, flags, strings)
        }

    }
}
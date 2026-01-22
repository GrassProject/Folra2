package com.github.grassproject.folra.item.component

import com.github.grassproject.folra.util.item.setSpawnerType
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.ItemMeta

class SpawnerTypeComponent(
    val entityType: EntityType
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemMeta: ItemMeta) {
        itemMeta.setSpawnerType(entityType)
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:spawner-type")

        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val entityType = section.getString("entity-type")?.uppercase()
                ?.let { runCatching { EntityType.valueOf(it) }.getOrNull() } ?: return null
            return SpawnerTypeComponent(entityType)
        }

    }
}
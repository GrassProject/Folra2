package com.github.grassproject.folra.registry.serializer

import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.ItemHandler
import com.github.grassproject.folra.item.component.*
import com.github.grassproject.folra.registry.FolraRegistry
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

object ItemSerializer {

    val itemComponentsFactories = hashSetOf(
        AmountComponent,
        CustomModelDataComponent,
        DamageComponent,
        DisplayNameComponent,
        DyeComponent,
        EnchantsComponent,
        FlagsComponent,
        ItemModelComponent,
        LoreComponent,
        MaxDamageComponent,
        MaxStackSizeComponent,
        SpawnerTypeComponent,
        TooltipStyleComponent,
        UnbreakableComponent
    )
    
    inline fun <reified T : Any> fromSection(
        section: ConfigurationSection?, crossinline mapper: (ConfigurationSection, FolraItem) -> T
    ): T? {
        val item = fromSection(section) ?: return null

        return mapper(section!!, item)
    }

    fun fromSection(
        section: ConfigurationSection?
    ): FolraItem? {
        section ?: return null
        return try {
            val material = section.getString("material", "STONE")!!
            val itemComponents = itemComponentsFactories.mapNotNull { it.load(section) }

            return create(
                material,
                itemComponents
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fromSections(sections: List<ConfigurationSection>): List<FolraItem> {
        return sections.mapNotNull { fromSection(it) }
    }

    inline fun <reified T : Any> fromSections(
        sections: List<ConfigurationSection>,
        crossinline mapper: (ConfigurationSection, FolraItem) -> T
    ): List<T> {
        return sections.mapNotNull { fromSection(it, mapper) }
    }

    fun createFactory(namespace: String): Pair<String?, ItemStack?> {
        if (namespace.contains(":")) {
            val (factoryId, itemId) = namespace.split(":", limit = 2).let {
                it[0].uppercase() to it[1]
            }

            val factory = FolraRegistry.ITEM_FACTORIES[factoryId]
            return factoryId to factory?.create(itemId)
        }
        val material = runCatching { Material.valueOf(namespace.uppercase()) }.getOrNull()
        return null to material?.let { ItemStack(it) }
    }

//    private fun create(
//        namespace: String,
//        itemComponents: List<ItemComponentHandle>
//    ): FolraItem? {
//        val (factoryId, itemStack) = createFactory(namespace)
//
//        if (itemStack == null) return null
//
//        return ItemHandler.create(
//            factoryId,
//            namespace,
//            itemStack,
//            itemComponents
//        )
//    }

    private fun create(
        namespace: String,
        itemComponents: List<ItemComponentHandle>
    ): FolraItem? {
        // var factoryId: String? = null
        val itemStack = if (namespace.contains(":")) {
            val id = namespace.split(":").first().uppercase()
            // factoryId = id

            val factory = FolraRegistry.ITEM_FACTORIES[id] ?: return null
            factory.create(namespace.substring(id.length + 1))
        } else {
            ItemStack(Material.valueOf(namespace.uppercase()))
        } ?: return null

        return ItemHandler.create(
            itemStack,
            itemComponents
        )
    }
}
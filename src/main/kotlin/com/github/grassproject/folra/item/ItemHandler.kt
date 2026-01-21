package com.github.grassproject.folra.item

import com.github.grassproject.folra.Folra
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object ItemHandler {

    val NAMESPACE_KEY by lazy {
        NamespacedKey(Folra.INSTANCE, "Custom_Item_Registry")
    }
    val listenInteractions = mutableMapOf<String, (FolraItemInteractEvent) -> Unit>()

    fun create(
        factoryId: String?,
        internalId: String?,
        item: ItemStack,
    ): FolraItem {
        return FolraItem(
            factoryId,
            internalId,
            item,
        )
    }

    interface Factory {

        fun create(id: String): ItemStack?

    }

}
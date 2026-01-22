package com.github.grassproject.folra.item1.factory

import com.github.grassproject.folra.item1.ItemHandler
import com.nexomc.nexo.api.NexoItems
import org.bukkit.inventory.ItemStack

object NexoFactory: ItemHandler.Factory {
    override fun create(id: String): ItemStack? {
        return NexoItems.itemFromId(id)?.build()
    }
}
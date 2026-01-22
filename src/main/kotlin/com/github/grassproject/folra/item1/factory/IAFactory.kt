package com.github.grassproject.folra.item1.factory

import com.github.grassproject.folra.item1.ItemHandler
import dev.lone.itemsadder.api.CustomStack
import org.bukkit.inventory.ItemStack

object IAFactory : ItemHandler.Factory {
    override fun create(id: String): ItemStack? {
        return CustomStack.getInstance(id)?.itemStack
    }
}
package com.github.grassproject.folra.item1.factory

import com.github.grassproject.folra.item1.ItemHandler
import com.github.grassproject.folra.util.item.ItemEncoder
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

object Base64Factory : ItemHandler.Factory {
    override fun create(id: String): ItemStack? {
        return ItemEncoder.decode(Base64Coder.decode(id))
    }
}
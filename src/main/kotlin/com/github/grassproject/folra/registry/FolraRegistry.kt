package com.github.grassproject.folra.registry

import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.factory.Base64Factory
import com.github.grassproject.folra.item.factory.CraftEngineFactory
import com.github.grassproject.folra.item.factory.IAFactory
import com.github.grassproject.folra.item.factory.NexoFactory

object FolraRegistry {

    val ITEM_FACTORIES = hashMapOf(
        "ITEMSADDER" to IAFactory,
        "CRAFTENGINE" to CraftEngineFactory,
        "BASE64" to Base64Factory,
        "NEXO" to NexoFactory
    )

    val ITEM = HashMap<String, FolraItem>()

}
package com.github.grassproject.folra.registry

import com.github.grassproject.folra.item1.FolraItem
import com.github.grassproject.folra.item1.factory.Base64Factory
import com.github.grassproject.folra.item1.factory.CraftEngineFactory
import com.github.grassproject.folra.item1.factory.IAFactory
import com.github.grassproject.folra.item1.factory.NexoFactory

object FolraRegistry {

    val ITEM_FACTORIES = hashMapOf(
        "ITEMSADDER" to IAFactory,
        "CRAFTENGINE" to CraftEngineFactory,
        "BASE64" to Base64Factory,
        "NEXO" to NexoFactory
    )

    val ITEM = HashMap<String, FolraItem>()

}
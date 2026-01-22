package com.github.grassproject.folra.registry

import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.FolraItemInteractEvent
import com.github.grassproject.folra.item.ItemHandler
import com.github.grassproject.folra.util.FolraKey
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

fun FolraItem.register(namespace: String, key: Key): Boolean =
    register(namespace, key, registerInteraction = false)

fun FolraItem.register(
    namespace: String,
    key: Key,
    interactionHandler: (FolraItemInteractEvent) -> Unit
): Boolean = register(namespace, key, interactionHandler, true)

private fun FolraItem.register(
    namespace: String,
    key: Key,
    interactionHandler: (FolraItemInteractEvent) -> Unit = {},
    registerInteraction: Boolean
): Boolean {
    val fullId = // "$namespace:${key.namespace()}:${key.value()}"
        FolraKey(namespace, key).toString()
    val item = getUnmodifiedItem()

    val existingId = registryId()
    if (existingId != null && existingId != fullId) return false

    item.editPersistentDataContainer { pdc ->
        pdc.set(ItemHandler.ITEM_KEY, PersistentDataType.STRING, fullId)
    }

    FolraRegistry.ITEM[fullId] = this

    if (registerInteraction) {
        ItemHandler.listenInteractions[fullId] = interactionHandler
    }

    return true
}

fun FolraItem.registryId(): String? {
    return getUnmodifiedItem().persistentDataContainer
        .get(ItemHandler.ITEM_KEY, PersistentDataType.STRING)
}

fun ItemStack.toFolraItem(): FolraItem? {
    val id = this.persistentDataContainer.get(
        ItemHandler.ITEM_KEY,
        PersistentDataType.STRING
    ) ?: return null

    return FolraRegistry.ITEM[id]
}

fun FolraItem.unregister(): Boolean {
    val id = registryId() ?: return false

    getUnmodifiedItem().editPersistentDataContainer { pdc ->
        pdc.remove(ItemHandler.ITEM_KEY)
    }

    ItemHandler.listenInteractions.remove(id)
    return FolraRegistry.ITEM.remove(id) != null
}
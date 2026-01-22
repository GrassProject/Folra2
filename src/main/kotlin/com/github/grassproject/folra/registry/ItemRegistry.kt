package com.github.grassproject.folra.registry

import com.github.grassproject.folra.item1.FolraItem
import com.github.grassproject.folra.item1.FolraItemImpl
import com.github.grassproject.folra.item1.FolraItemInteractEvent
import com.github.grassproject.folra.item1.ItemHandler
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

fun FolraItem.register(namespace: String, id: String): Boolean =
    register(namespace, id, registerInteraction = false)

fun FolraItem.register(
    namespace: String,
    id: String,
    interactionHandler: (FolraItemInteractEvent) -> Unit
): Boolean = register(namespace, id, interactionHandler, true)

private fun FolraItem.register(
    namespace: String,
    id: String,
    interactionHandler: (FolraItemInteractEvent) -> Unit = {},
    registerInteraction: Boolean
): Boolean {
    val fullId = "$namespace:$id"
    val id = registryId()
    val item = getUnmodifiedItem()

    if (id != null && id != fullId) return false

    item.editPersistentDataContainer {
        it[ItemHandler.ITEM_KEY, PersistentDataType.STRING] = fullId
    }

    println("PDC: ${item.persistentDataContainer.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING)}")

    FolraRegistry.ITEM[fullId] = this

    if (registerInteraction) {
        ItemHandler.listenInteractions[fullId] = interactionHandler
    }

    return true
}

fun FolraItem.setInteractionHandler(interactionHandler: (FolraItemInteractEvent) -> Unit): Boolean {
    val id = registryId() ?: return false
    ItemHandler.listenInteractions[id] = interactionHandler
    return true
}

fun FolraItem.removeInteractionHandler(): Boolean {
    val id = registryId() ?: return false
    return ItemHandler.listenInteractions.remove(id) != null
}

fun FolraItem.unregister(): Boolean {
    val id = registryId() ?: return false
    val item = getUnmodifiedItem()

    item.editMeta {
        it.persistentDataContainer.remove(ItemHandler.ITEM_KEY)
    }

    val removed = FolraRegistry.ITEM.remove(id)
    ItemHandler.listenInteractions.remove(id)
    return removed != null
}

fun FolraItem.registryId(): String? {
//    val pdc = getUnmodifiedItem().persistentDataContainer
//    println(pdc.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING))
//    return pdc.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING)
    val meta = getUnmodifiedItem().itemMeta
    val pdc = meta?.persistentDataContainer ?: return null
    println(pdc.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING))
    println(getUnmodifiedItem().persistentDataContainer.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING))
    return pdc.get(ItemHandler.ITEM_KEY, PersistentDataType.STRING)
}

fun ItemStack.toFolraItem(): FolraItem? {
    val pdc = persistentDataContainer
    val namespacedKey = ItemHandler.ITEM_KEY
    if (!pdc.has(namespacedKey, PersistentDataType.STRING)) return null
    val id = pdc.get(namespacedKey, PersistentDataType.STRING) ?: return null
    return FolraRegistry.ITEM[id]
}
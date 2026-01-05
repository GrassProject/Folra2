package com.github.grassproject.folra.registry

import com.github.grassproject.folra.item.FolraItem
import com.github.grassproject.folra.item.FolraItemInteractEvent
import com.github.grassproject.folra.item.ItemHandler
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

fun FolraItem.register(
    namespace: String,
    id: String
): Boolean {
    return register(
        namespace, id,
        registerInteraction = false
    )
}

fun FolraItem.register(
    namespace: String,
    id: String,
    interactionHandler: (FolraItemInteractEvent) -> Unit
): Boolean {
    return register(namespace, id, interactionHandler, true)
}

private fun FolraItem.register(
    namespace: String, id: String,
    interactionHandler: (FolraItemInteractEvent) -> Unit = {}, registerInteraction: Boolean
): Boolean {
    val registryId = registryId()
    val item = getUnmodifiedItem()
    if (registryId != null && registryId != "$namespace:$id") return false

    item.editPersistentDataContainer {
        it.set(ItemHandler.NAMESPACE_KEY, PersistentDataType.STRING, "$namespace:$id")
    }

//    Registry.update {
//        replaceRegistry(
//            FolraItem.ITEM_REGISTRY_KEY
//        ) {
//            register("$namespace:$id", this@register)
//        }
//    }
    FolraRegistry.ITEM["$namespace:$id"] = this

    if (registerInteraction) {
        ItemHandler.listenInteractions["$namespace:$id"] = interactionHandler
    }
    return true
}

fun FolraItem.setInteractionHandler(interactionHandler: (FolraItemInteractEvent) -> Unit): Boolean {
    val registryId = registryId() ?: return false
    ItemHandler.listenInteractions[registryId] = interactionHandler
    return true
}

fun FolraItem.removeInteractionHandler(): Boolean {
    val registryId = registryId() ?: return false
    ItemHandler.listenInteractions.remove(registryId)
    return true
}

fun FolraItem.unregister(): Boolean {
    val registryId = registryId() ?: return false
    val item = getUnmodifiedItem()
    item.editPersistentDataContainer {
        it.remove(ItemHandler.NAMESPACE_KEY)
    }

//    var found = false
//    Registry.update {
//        replaceRegistry(
//            FolraItem.ITEM_REGISTRY_KEY
//        ) {
//            if (unregister(registryId) != null) found = true
//        }
//    }
//    if (!found) return false

    FolraRegistry.ITEM.remove(registryId)
    ItemHandler.listenInteractions.remove(registryId)
    return true
}

fun FolraItem.registryId(): String? {
    val pdc = getUnmodifiedItem().persistentDataContainer
    return pdc.get(ItemHandler.NAMESPACE_KEY, PersistentDataType.STRING)
}

fun ItemStack.toFolraItem(): FolraItem? {
    val pdc = persistentDataContainer
    val namespacedKey = ItemHandler.NAMESPACE_KEY
    if (!pdc.has(namespacedKey, PersistentDataType.STRING)) return null
    val id = pdc.get(namespacedKey, PersistentDataType.STRING) ?: return null
    return FolraRegistry.ITEM[id]
}
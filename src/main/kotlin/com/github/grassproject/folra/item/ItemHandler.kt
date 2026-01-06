package com.github.grassproject.folra.item

import com.github.grassproject.folra.Folra
import com.github.grassproject.folra.api.event.call
import com.github.grassproject.folra.api.event.event
import com.github.grassproject.folra.item.option.ItemOptionHandle
import com.github.grassproject.folra.registry.registryId
import com.github.grassproject.folra.registry.toFolraItem
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object ItemHandler {

    val NAMESPACE_KEY by lazy {
        NamespacedKey(Folra.INSTANCE, "Custom_Item_Registry")
    }
    val listenInteractions = mutableMapOf<String, (FolraItemInteractEvent) -> Unit>()

    fun initialize() {
        event<PlayerInteractEvent> {
            handleInteract(it)
        }

        event<PlayerSwapHandItemsEvent> {
            handleSwapHandItems(it)
        }

        event<InventoryClickEvent> {
            handleInventoryClick(it)
        }
    }

    fun create(
        factoryId: String?,
        internalId: String?,
        item: ItemStack,
        options: List<ItemOptionHandle>
    ): FolraItem {
        return FolraItem(
            factoryId,
            internalId,
            item,
            options
        )
    }

    private fun handleInteract(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.OFF_HAND) return
        if (listenInteractions.isEmpty()) return
        val item = event.item ?: return
        val aitem = item.toFolraItem() ?: return
        val registry = aitem.registryId() ?: return

        val interaction = listenInteractions[registry] ?: return

        val interactType = when (event.action) {
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> if (event.player.isSneaking) FolraItemInteractEvent.InteractType.SHIFT_RIGHT else FolraItemInteractEvent.InteractType.RIGHT
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> if (event.player.isSneaking) FolraItemInteractEvent.InteractType.SHIFT_LEFT else FolraItemInteractEvent.InteractType.LEFT
            else -> return
        }

        val aitemEvent = FolraItemInteractEvent(
            event.player, aitem, item, event, interactType,
        )
        interaction(aitemEvent)
        aitemEvent.call()
    }

    private fun handleSwapHandItems(event: PlayerSwapHandItemsEvent) {
        val item = event.mainHandItem
        val aitem = item.toFolraItem() ?: return
        val registry = aitem.registryId() ?: return
        val interaction = listenInteractions[registry] ?: return

        val interactType =
            if (event.player.isSneaking) FolraItemInteractEvent.InteractType.SHIFT_SWAP else FolraItemInteractEvent.InteractType.SWAP
        val aitemEvent = FolraItemInteractEvent(
            event.player, aitem, item, event, interactType,
        )
        interaction(aitemEvent)
        aitemEvent.call()
    }

    private fun handleInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val item = event.currentItem ?: return
        val aitem = item.toFolraItem() ?: return
        val registry = aitem.registryId() ?: return
        val interaction = listenInteractions[registry] ?: return

        val interactType = when (event.click) {
            ClickType.SHIFT_LEFT -> FolraItemInteractEvent.InteractType.INVENTORY_SHIFT_LEFT
            ClickType.LEFT -> FolraItemInteractEvent.InteractType.INVENTORY_LEFT
            ClickType.SHIFT_RIGHT -> FolraItemInteractEvent.InteractType.INVENTORY_SHIFT_RIGHT
            ClickType.RIGHT -> FolraItemInteractEvent.InteractType.INVENTORY_RIGHT
            ClickType.NUMBER_KEY -> {
                when (event.hotbarButton) {
                    1 -> FolraItemInteractEvent.InteractType.NUM_1
                    2 -> FolraItemInteractEvent.InteractType.NUM_2
                    3 -> FolraItemInteractEvent.InteractType.NUM_3
                    4 -> FolraItemInteractEvent.InteractType.NUM_4
                    5 -> FolraItemInteractEvent.InteractType.NUM_5
                    6 -> FolraItemInteractEvent.InteractType.NUM_6
                    7 -> FolraItemInteractEvent.InteractType.NUM_7
                    8 -> FolraItemInteractEvent.InteractType.NUM_8
                    9 -> FolraItemInteractEvent.InteractType.NUM_9
                    else -> FolraItemInteractEvent.InteractType.NUM_0
                }
            }

            ClickType.DROP -> FolraItemInteractEvent.InteractType.INVENTORY_DROP
            ClickType.SWAP_OFFHAND -> FolraItemInteractEvent.InteractType.INVENTORY_SWAP
            else -> return
        }
        val aitemEvent = FolraItemInteractEvent(
            player,
            aitem,
            item,
            event,
            interactType,
        )
        interaction(aitemEvent)
        aitemEvent.call()
    }

    interface Factory {

        fun create(id: String): ItemStack?

    }

}
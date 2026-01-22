//package com.github.grassproject.folra.item
//
//import com.github.grassproject.folra.item.component.ItemComponentHandle
//import com.github.grassproject.folra.registry.serializer.ItemSerializer
//import net.kyori.adventure.key.Key
//import org.bukkit.configuration.ConfigurationSection
//import org.bukkit.inventory.ItemStack
//import org.bukkit.inventory.ItemType
//
//interface FolraItem {
//
//    val components: Collection<ItemComponentHandle>
//
//    fun getUnmodifiedItem(): ItemStack
//    fun getItem(): ItemStack
//
//    companion object {
//        fun loadFromYml(section: ConfigurationSection?): FolraItem? {
//            return ItemSerializer.fromSection(section)
//        }
//    }
//}
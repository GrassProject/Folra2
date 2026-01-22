package com.github.grassproject.folra.item.component

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

class EnchantsComponent(
    val enchants: Map<String, Int>
) : ItemComponentHandle {

    override val key: Key = Companion.key

    override fun apply(itemStack: ItemStack) {
        for ((enchant, level) in enchants) {
            getEnchantmentByString(enchant)?.apply {
                itemStack.addUnsafeEnchantment(this, level)
            }
        }
    }

    override fun apply(itemMeta: ItemMeta) {
        if (itemMeta is EnchantmentStorageMeta) {
            for ((enchant, level) in enchants) {
                if (enchant.uppercase().startsWith("AE-")) continue
                if (enchant.uppercase() == "AE-SLOTS") continue

                getEnchantmentByString(enchant)?.apply {
                    itemMeta.addStoredEnchant(this, level, true)
                }
            }
        }
    }

    private fun getEnchantmentByString(enchant: String): Enchantment? {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
            .get(NamespacedKey.minecraft(enchant.lowercase(Locale.getDefault())))
    }

    companion object : ItemComponentLoader {
        override val key: Key = Key.key("itemcomponent:enchants")
        override fun load(section: ConfigurationSection): ItemComponentHandle? {
            val enchantments = section.getStringList("enchants")
                .mapNotNull {
                    val parts = it.split(":")
                    if (parts.size < 2) return@mapNotNull null

                    val name = parts[0].trim()
                    val level = parts[1].trim().toIntOrNull() ?: return@mapNotNull null
                    name to level
                }
                .toMap()
                .takeIf { it.isNotEmpty() } ?: return null
            return EnchantsComponent(enchantments)
        }

    }
}
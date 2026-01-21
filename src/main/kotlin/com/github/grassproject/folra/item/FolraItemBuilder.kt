package com.github.grassproject.folra.item

import com.github.grassproject.folra.annotation.FolraDsl
import com.github.grassproject.folra.item.data.CustomModelData
import com.github.grassproject.folra.registry.serializer.ItemSerializer
import com.github.grassproject.folra.util.item.setDisplayName
import com.github.grassproject.folra.util.item.setSpawnerType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.util.*
import io.papermc.paper.datacomponent.item.CustomModelData as PaperCustomModelData

@FolraDsl
class FolraItemBuilder(private val baseStack: ItemStack) {

    constructor(namespace: String) : this(ItemSerializer.createFactory(namespace).second ?: ItemStack(Material.AIR))

    var amount: Int = 1
        set(value) {
            baseStack.amount = value
            field = value
        }

    var displayName: Component? = null
        set(value) {
            value?.let { baseStack.setDisplayName(value) }
            field = value
        }

    val lore = mutableListOf<Component>()

    fun customModelData(builder: CustomModelData.Builder.() -> Unit) {
        val data = CustomModelData.Builder().apply(builder).build()
        val paperData = PaperCustomModelData.customModelData()
            .addColors(data.colors)
            .addFloats(data.floats)
            .addFlags(data.flags)
            .addStrings(data.strings)

        baseStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, paperData)
    }

    var itemModel: Key? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.ITEM_MODEL, it) }
            field = value
        }

    var damage: Int? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.DAMAGE, it) }
            field = value
        }

    var maxDamage: Int? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.MAX_DAMAGE, it) }
            field = value
        }

    var maxStackSize: Int? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.MAX_STACK_SIZE, it) }
            field = value
        }

    var unbreakable: Boolean = false
        set(value) {
            if (value) {
                baseStack.setData(DataComponentTypes.UNBREAKABLE)
            } else {
                baseStack.unsetData(DataComponentTypes.UNBREAKABLE)
            }
            field = value
        }

    var rarity: ItemRarity? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.RARITY, it) }
            field = value
        }

    var spawnerType: EntityType? = null
        set(value) {
            value?.let { baseStack.setSpawnerType(it) }
            field = value
        }

    var tooltipStyle: Key? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.TOOLTIP_STYLE, it) }
            field = value
        }

    var dyeColor: Color? = null
        set(value) {
            value?.let { baseStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(it)) }
            field = value
        }

    val enchants = mutableMapOf<String, Int>()
    val flags = mutableSetOf<ItemFlag>()

    fun lore(vararg lines: Component) {
        lore.addAll(lines)
    }

    fun flag(vararg itemFlags: ItemFlag) {
        flags.addAll(itemFlags)
    }

    fun enchant(name: String, level: Int) {
        enchants[name] = level
    }

    fun build(): FolraItem {
        if (lore.isNotEmpty()) {
            baseStack.lore(lore)
        }
//        if (clientsideLore.isNotEmpty()) {
//            options[ClientsideLoreOptionHandler::class.java] = ClientsideLoreOptionHandler(lore)
//        }
        if (enchants.isNotEmpty()) {
            for ((enchant, level) in enchants) {
                getEnchantmentByString(enchant)?.apply {
                    baseStack.addUnsafeEnchantment(this, level)
                }
            }
            baseStack.setAEEnchants()
        }
        if (flags.isNotEmpty()) {
            baseStack.addItemFlags(*flags.toTypedArray())
        }

        return FolraItem(
            factoryId = null,
            internalId = null,
            item = baseStack,
        )
    }

    private fun getEnchantmentByString(ench: String): Enchantment? {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
            .get(NamespacedKey.minecraft(ench.lowercase(Locale.getDefault())))
    }

    fun ItemStack.setAEEnchants() {
        val meta = itemMeta ?: return
        if (meta is EnchantmentStorageMeta) {
            for ((enchant, level) in enchants) {
                if (enchant.uppercase().startsWith("AE-")) continue
                if (enchant.uppercase() == "AE-SLOTS") continue

                getEnchantmentByString(enchant)?.apply {
                    itemMeta.addEnchant(this, level, true)
                }
            }
        }
        itemMeta = meta
    }

}

fun folraItem(material: Material, builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(ItemStack(material)).apply(builder).build()
}

fun folraItem(namespace: String, builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(namespace).apply(builder).build()
}

fun ItemStack.toFolraBuilder(builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(this.clone()).apply(builder).build()
}

fun Material.toFolraBuilder(builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(ItemStack(this)).apply(builder).build()
}
package com.github.grassproject.folra.item

import com.github.grassproject.folra.annotation.FolraDsl
import com.github.grassproject.folra.item.component.*
import com.github.grassproject.folra.registry.serializer.ItemSerializer
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@FolraDsl
class FolraItemBuilder(
    private val baseStack: ItemStack
) {

    private val components = mutableMapOf<Class<out ItemComponentHandle>, ItemComponentHandle>()

    var amount: Int = 1
        set(value) {
            components[AmountComponent::class.java] = AmountComponent(value)
            field = value
        }

    var displayName: Component? = null
        set(value) {
            value?.let { components[DisplayNameComponent::class.java] = DisplayNameComponent(it) }
            field = value
        }

    val lore = mutableListOf<Component>()
    fun lore(vararg lines: Component) {
        this.lore.addAll(lines)
    }
    fun lore(vararg lines: String) {
        lines.forEach { this.lore.add(Component.text(it)) }
    }

    fun customModelData(builder: CustomModelDataBuilder.() -> Unit) {
        val dataBuilder = CustomModelDataBuilder().apply(builder)
        components[CustomModelDataComponent::class.java] = dataBuilder.build()
    }

    var itemModel: Key? = null
        set(value) {
            value?.let { components[ItemModelComponent::class.java] = ItemModelComponent(it) }
            field = value
        }

    var damage: Int? = null
        set(value) {
            value?.let { components[DamageComponent::class.java] = DamageComponent(it) }
            field = value
        }

    var maxDamage: Int? = null
        set(value) {
            value?.let { components[MaxDamageComponent::class.java] = MaxDamageComponent(it) }
            field = value
        }

    var maxStackSize: Int? = null
        set(value) {
            value?.let { components[MaxStackSizeComponent::class.java] = MaxStackSizeComponent(it) }
            field = value
        }

    var unbreakable: Boolean = false
        set(value) {
            if (!value) {
                components -= UnbreakableComponent::class.java
            } else {
                components[UnbreakableComponent::class.java] = UnbreakableComponent()
            }
            field = value
        }
    

    var spawnerType: EntityType? = null
        set(value) {
            value?.let { components[SpawnerTypeComponent::class.java] = SpawnerTypeComponent(it) }
            field = value
        }

    var tooltipStyle: Key? = null
        set(value) {
            value?.let { components[TooltipStyleComponent::class.java] = TooltipStyleComponent(it) }
            field = value
        }

    var dyeColor: Color? = null
        set(value) {
            value?.let { components[DyeComponent::class.java] = DyeComponent(it) }
            field = value
        }

    val enchants = mutableMapOf<String, Int>()
    fun enchants(builder: EnchantmentBuilder.() -> Unit) {
        val enchantBuilder = EnchantmentBuilder().apply(builder)
        components[EnchantsComponent::class.java] = enchantBuilder.build()
    }

    val flags = mutableSetOf<ItemFlag>()
    fun flags(vararg flags: ItemFlag) {
        this.flags.addAll(flags)
    }

    fun build(): FolraItem {
        if (lore.isNotEmpty()) {
            components[LoreComponent::class.java] = LoreComponent(lore)
        }
        if (enchants.isNotEmpty()) {
            components[EnchantsComponent::class.java] = EnchantsComponent(enchants)
        }
        if (flags.isNotEmpty()) {
            components[FlagsComponent::class.java] = FlagsComponent(flags.toList())
        }

        return FolraItem(
            baseStack,
            components.values
        )

    }
}

fun folraItem(material: Material, builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(ItemStack(material)).apply(builder).build()
}

fun folraItem(namespace: String, builder: FolraItemBuilder.() -> Unit): FolraItem {
    val baseStack = ItemSerializer.createFactory(namespace).second ?: ItemStack(Material.AIR)
    return FolraItemBuilder(baseStack).apply(builder).build()
}

fun ItemStack.toFolraBuilder(builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(this.clone()).apply(builder).build()
}

fun Material.toFolraBuilder(builder: FolraItemBuilder.() -> Unit): FolraItem {
    return FolraItemBuilder(ItemStack(this)).apply(builder).build()
}

@FolraDsl
class CustomModelDataBuilder {
    val colors = mutableListOf<Color>()
    val floats = mutableListOf<Float>()
    val flags = mutableListOf<Boolean>()
    val strings = mutableListOf<String>()

    fun build() = CustomModelDataComponent(
        colors.toList(), floats.toList(), flags.toList(), strings.toList()
    )
}

@FolraDsl
class EnchantmentBuilder {
    val enchants = mutableMapOf<String, Int>()

    operator fun Enchantment.invoke(level: Int) {
        enchants[this.key.value()] = level
    }

    infix fun Enchantment.level(level: Int) {
        enchants[this.key.value()] = level
    }

    fun build(): EnchantsComponent {
        return EnchantsComponent(enchants.toMap())
    }
}

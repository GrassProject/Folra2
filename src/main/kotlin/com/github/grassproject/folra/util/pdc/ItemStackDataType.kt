package com.github.grassproject.folra.util.pdc

import com.github.grassproject.folra.util.item.ItemEncoder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object ItemStackDataType : PersistentDataType<ByteArray, ItemStack> {
    override fun getPrimitiveType(): Class<ByteArray> = ByteArray::class.java
    override fun getComplexType(): Class<ItemStack> = ItemStack::class.java

    override fun toPrimitive(complex: ItemStack, context: PersistentDataAdapterContext): ByteArray {
        return ItemEncoder.encode(complex)
    }

    override fun fromPrimitive(primitive: ByteArray, ontext: PersistentDataAdapterContext): ItemStack {
        return ItemEncoder.decode(primitive)
    }
}
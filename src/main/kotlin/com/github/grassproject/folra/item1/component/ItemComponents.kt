package com.github.grassproject.folra.item1.component

import net.kyori.adventure.key.Key

enum class ItemComponents(val key: Key) {
    AMOUNT(AmountComponent.key),
    CUSTOM_MODEL_DATA(CustomModelDataComponent.key),
    DAMAGE(DamageComponent.key),
    DISPLAY_NAME(DisplayNameComponent.key),
    DYE(DyeComponent.key),
    ENCHANTS(EnchantsComponent.key),
    FLAGS(FlagsComponent.key),
    ITEM_MODEL(ItemModelComponent.key),
    LORE(LoreComponent.key),
    MAX_DAMAGE(MaxDamageComponent.key),
    MAX_STACK_SIZE(MaxStackSizeComponent.key),
    SPAWNER_TYPE(SpawnerTypeComponent.key),
    TOOLTIP_STYLE(TooltipStyleComponent.key),
    UNBREAKABLE(UnbreakableComponent.key)
}
package com.github.grassproject.folra.inventory

import com.github.grassproject.folra.api.event.packet.PacketContainerClickEvent

class AccumulatedDrag(
    val packet: PacketContainerClickEvent,
    val type: ClickType
)
package com.github.grassproject.folra.api.event

abstract class PacketEvent : CancellableFolraEvent(true) {

    var then: () -> Unit = {}

}

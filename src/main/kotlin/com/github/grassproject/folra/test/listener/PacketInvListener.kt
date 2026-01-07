package com.github.grassproject.folra.test.listener

import com.github.grassproject.folra.api.event.event
import com.github.grassproject.folra.api.event.packet.PacketContainerClickEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerCloseEvent
import com.github.grassproject.folra.api.event.packet.PacketContainerOpenEvent
import org.bukkit.event.Listener

object PacketInvListener {

//    @EventHandler
//    fun PacketContainerOpenEvent.on() {
//        this.player.sendMessage("open packet inv : ${this.containerId}")
//    }
//
//    @EventHandler
//    fun PacketContainerCloseEvent.on() {
//        this.player.sendMessage("close packet inv")
//    }
//
//    @EventHandler
//    fun Packet

    fun init() {
        event<PacketContainerOpenEvent> {
            it.player.sendMessage("open packet inv: ${it.containerId}")
        }

        event<PacketContainerCloseEvent> {
            it.player.sendMessage("close packet inv")
        }

        event<PacketContainerClickEvent> {
            println(it.player)
            println(it.containerId)
            println(it.stateId)
            println(it.slotNum)
            println(it.buttonNum)
            println(it.clickTypeId)
            println(it.carriedItem)
            println(it.changedSlots)
        }
    }
}
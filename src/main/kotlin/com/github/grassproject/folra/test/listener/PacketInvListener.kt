//package com.github.grassproject.folra.test.listener
//
//import com.github.grassproject.folra.api.event.event
//import com.github.grassproject.folra.api.event.packet.PacketContainerClickEvent
//import com.github.grassproject.folra.api.event.packet.PacketContainerCloseEvent
//import com.github.grassproject.folra.api.event.packet.PacketContainerOpenEvent
//import com.github.grassproject.folra.inventory.event.AsyncPacketInventoryCloseEvent
//import com.github.grassproject.folra.inventory.event.AsyncPacketInventoryInteractEvent
//import org.bukkit.event.Listener
//
//object PacketInvListener {
//
////    @EventHandler
////    fun PacketContainerOpenEvent.on() {
////        this.player.sendMessage("open packet inv : ${this.containerId}")
////    }
////
////    @EventHandler
////    fun PacketContainerCloseEvent.on() {
////        this.player.sendMessage("close packet inv")
////    }
////
////    @EventHandler
////    fun Packet
//
//    fun init() {
////        event<PacketContainerOpenEvent> {
////            it.player.sendMessage("open packet inv: ${it.containerId}")
////        }
////
////        event<PacketContainerCloseEvent> {
////            it.player.sendMessage("close packet inv")
////        }
////
////        event<PacketContainerClickEvent> {
////            println("Debug - PacketContainerClickEvent Start")
////            println(it.player)
////            println(it.containerId)
////            println(it.stateId)
////            println(it.slotNum)
////            println(it.buttonNum)
////            println(it.clickTypeId)
////            println(it.carriedItem)
////            println(it.changedSlots)
////            println("Debug - PacketContainerClickEvent End")
////        }
//
//        event<AsyncPacketInventoryCloseEvent> {
//            println("Debug - AsyncPacketInventoryCloseEvent Start")
//            println(it.player)
//            println(it.inventory.title)
//            println(it.inventory.viewers)
//            println(it.inventory.type)
//            println("Debug - AsyncPacketInventoryCloseEvent End")
//        }
//
//        event<AsyncPacketInventoryInteractEvent> {
//            println("Debug - AsyncPacketInventoryInteractEvent Start")
//            println(it.viewer)
//            println(it.slot)
//            println(it.buttonType)
//            println(it.cursor)
//            println(it.buttonType)
//            println(it.slots)
//            println("Debug - AsyncPacketInventoryInteractEvent End")
//        }
//    }
//}
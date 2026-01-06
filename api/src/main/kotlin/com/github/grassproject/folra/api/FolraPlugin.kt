package com.github.grassproject.folra.api

import org.bukkit.plugin.java.JavaPlugin

abstract class FolraPlugin : JavaPlugin(), IFolraPlugin {

    companion object {
        lateinit var INSTANCE: FolraPlugin
    }

    override fun onLoad() {
        //
        load()
    }

    override fun onEnable() {
        //
        enable()
    }

    override fun onDisable() {
        //
        disable()
    }
}
package com.github.grassproject.folra.api

import org.bukkit.plugin.java.JavaPlugin

abstract class FolraPlugin : JavaPlugin(), IFolraPlugin {

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
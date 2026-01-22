package com.github.grassproject.folra.module

import com.github.grassproject.folra.BukkitFolraPlugin

interface FolraModules {

    fun register(folra: BukkitFolraPlugin)
    fun unregister(folra: BukkitFolraPlugin)

}
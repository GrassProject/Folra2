package com.github.grassproject.folra

import com.github.grassproject.folra.api.FolraPlugin
import com.github.grassproject.folra.api.nms.NMSManager

class Folra : FolraPlugin() {

    companion object {
        lateinit var INSTANCE: Folra
            private set
    }

    override fun load() {
        INSTANCE = this
        NMSManager.init(this)
    }

    override fun onEnable() {
        NMSManager.setup()
    }
}
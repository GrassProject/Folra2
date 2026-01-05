package com.github.grassproject.folra

import com.github.grassproject.folra.api.FolraPlugin

class Folra : FolraPlugin() {

    companion object {
        lateinit var INSTANCE: Folra
            private set
    }

    override fun load() {
        INSTANCE = this
    }
}
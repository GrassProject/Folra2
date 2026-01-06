package com.github.grassproject.folra.module

import com.github.grassproject.folra.Folra

interface FolraModules {

    fun register(folra: Folra)
    fun unregister(folra: Folra)

}
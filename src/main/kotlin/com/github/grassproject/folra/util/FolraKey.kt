package com.github.grassproject.folra.util

import com.github.grassproject.folra.api.FolraPlugin
import net.kyori.adventure.key.Key

data class FolraKey(
    private val namespace: String,
    private val category: String,
    private val value: String
) : Key {

    override fun namespace(): String = namespace.toKeyStandard()
    fun category(): String = category.toKeyStandard()
    override fun value(): String = value.toKeyStandard()

    constructor(plugin: FolraPlugin, key: Key) : this(
        plugin.namespace(),
        key.namespace(),
        key.value()
    )
    constructor(namespace: String, key: Key) : this(
        namespace,
        key.namespace(),
        key.value()
    )

    private val identity = "${namespace()}:${category()}:${value()}"

    override fun toString(): String = identity
    override fun asString(): String = identity

    override fun equals(other: Any?): Boolean =
        if (this === other) true else other is FolraKey && other.identity == this.identity

    override fun hashCode(): Int = identity.hashCode()

    companion object {
        fun fromString(fullKey: String): FolraKey? {
            val parts = fullKey.split(":")
            if (parts.size != 3) return null
            return FolraKey(parts[0], parts[1], parts[2])
        }

        fun folra(category: String, value: String): FolraKey {
            return FolraKey("folra", category, value)
        }
    }
}
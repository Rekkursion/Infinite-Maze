package com.rekkursion.infinitemaze.utils

enum class BlockType(val symbol: String, val walkable: Boolean) {
    START("S", true),
    END("E", true),
    PATH(".", true),
    WALL("#", false)
}
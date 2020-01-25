package com.rekkursion.infinitemaze.utils

import android.graphics.Color

enum class BlockType(
    val symbol: String,
    val walkable: Boolean,
    val colorValueOnMazeView: Int
) {
    START("S", true, Color.rgb(233, 233, 233)),
    END("E", true, Color.RED),
    PATH(".", true, Color.rgb(233, 233, 233)),
    WALL("#", false, Color.BLACK),
    CURRENT("X", false, Color.rgb(255, 204, 153))
}
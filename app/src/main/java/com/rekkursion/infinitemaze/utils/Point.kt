package com.rekkursion.infinitemaze.utils

class Point<T>(y: T, x: T) {
    // the second value of this point
    private var mY = y
    var y get() = mY; set(value) { mY = value }

    // the first value of this point
    private var mX = x
    var x get() = mX; set(value) { mX = value }

    /* ================================================================ */

    // copy constructor
    constructor(pt: Point<T>): this(pt.y, pt.x)

    /* ================================================================ */

    // set a new location
    fun set(newY: T = mY, newX: T = mX): Point<T> { mY = newY; mX = newX; return this }

    /* ================================================================ */

    operator fun component1(): T = mY

    operator fun component2(): T = mX

    /* ================================================================ */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point<*>

        if (mX != other.mX) return false
        if (mY != other.mY) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mX?.hashCode() ?: 0
        result = 31 * result + (mY?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return String.format("(Y: %02d, X: %02d)", mY, mX)
    }
}
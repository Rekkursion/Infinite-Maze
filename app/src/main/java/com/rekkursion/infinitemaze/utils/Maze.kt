package com.rekkursion.infinitemaze.utils

class Maze private constructor() {
    class Builder {
        private val mInstance = Maze()

        // set the level of this maze
        fun setLevel(level: Int): Builder {
            mInstance.mLevel = level
            return this
        }

        // set the size of this maze
        fun setSize(width: Int = mInstance.mWidth, height: Int = mInstance.mHeight): Builder {
            mInstance.mWidth = width
            mInstance.mHeight = height
            return this
        }

        // set if this maze is closed or not
        fun setIsClosed(isClosed: Boolean): Builder {
            mInstance.mIsClosed = isClosed
            return this
        }

        // set the start location of this maze
        fun setStartLocation(
            y: Int = if (mInstance.mIsClosed && mInstance.mHeight > 1) 1 else 0,
            x: Int = if (mInstance.mIsClosed && mInstance.mWidth > 1) 1 else 0
        ): Builder {
            mInstance.mStartLoc.set(y, x)
            mInstance.mCurLoc.set(y, x)
            return this
        }

        // set the end location of this maze
        fun setEndLocation(
            y: Int = mInstance.mHeight - if (mInstance.mIsClosed && mInstance.mHeight > 1) 2 else 1,
            x: Int = mInstance.mWidth - if (mInstance.mIsClosed && mInstance.mWidth > 1) 2 else 1
        ): Builder {
            mInstance.mEndLoc.set(y, x)
            return this
        }

        // create this maze
        fun create(): Maze {
            return mInstance.generate()
        }
    }

    /* ================================================================ */

    private var mLevel: Int? = null
    val level get() = mLevel

    // width of the maze
    private var mWidth = 1
    val width get() = mWidth

    // height of the maze
    private var mHeight = 1
    val height get() = mHeight

    // if the maze shall be closed or not
    private var mIsClosed = true

    // the start location of the maze
    private var mStartLoc = Point(0, 0)

    // the end location of the maze
    private var mEndLoc = Point(0, 0)
    val endLocCopied get() = Point(mEndLoc)

    // the current location, initially set to the start location
    private var mCurLoc = Point(mStartLoc)
    val curLocCopied get() = Point(mCurLoc)
    val curY get() = curLocCopied.y
    val curX get() = curLocCopied.x

    // the content of the maze
    private lateinit var mBlocks: Array<Array<BlockType>>
    val blocksCopied: Array<Array<BlockType>> get() {
        val ret = Array(mBlocks.size) { Array(mBlocks[0].size) { BlockType.WALL } }
        for (k in 0 until mBlocks.size) {
            for (j in 0 until mBlocks[0].size)
                ret[k][j] = mBlocks[k][j]
        }
        return ret
    }

    /* ================================================================ */

    // move the current location
    fun moveCurrentLocation(dy: Int = 0, dx: Int = 0): Point<Int> {
        val newY = mCurLoc.y + dy
        val newX = mCurLoc.x + dx

        return if (newY >= 0 && newX >= 0 && newY < mHeight && newX < mWidth && mBlocks[newY][newX].walkable)
            mCurLoc.set(newY, newX)
        else
            mCurLoc
    }

    /* ================================================================ */

    // generate paths of the maze and return itself
    private fun generate(): Maze {
        // initially set all blocks into WALL
        mBlocks = Array(mHeight) { Array(mWidth) { BlockType.WALL } }

        // set START
        mBlocks[mStartLoc.y][mStartLoc.x] = BlockType.START
        // set END
        mBlocks[mEndLoc.y][mEndLoc.x] = BlockType.END

        // recursively generate paths by dfs
        generateRecursively(mStartLoc.y, mStartLoc.x)

        // return itself
        return this
    }

    // helper function: generate paths by dfs
    private fun generateRecursively(curY: Int, curX: Int) {
        // create the direction mutable-list and shuffle it
        val dirs = mutableListOf(Point(-1, 0), Point(0, 1), Point(1, 0), Point(0, -1))
        dirs.shuffle()

        // iterate 4 kinds of directions (up, right, down, left)
        dirs.forEach { (dy, dx) ->
            // calculate the adjacent location
            val newY = curY + dy
            val newX = curX + dx

            // judge if that location could be a path-block or not
            if (canBePathBlockAt(newY, newX)) {
                mBlocks[newY][newX] = BlockType.PATH
                generateRecursively(newY, newX)
            }
        }
    }

    // helper function: judge if a certain location could be a path-block or not
    private fun canBePathBlockAt(y: Int, x: Int): Boolean {
        // if the passed (y, x) is out of range -> return false directly
        if (y < 0 || x < 0 || y >= mHeight || x >= mWidth) return false

        // if the maze is closed -> the location at the outline cannot be a path-block
        if (mIsClosed && (y == 0 || x == 0 || y == mHeight - 1 || x == mWidth - 1)) return false

        val curBlock = mBlocks[y][x]
        // if the passed (y, x) is a start- or an end- block -> return false
        if (curBlock == BlockType.START || curBlock == BlockType.END) return false
        // if the passed (y, x) is already a path-block -> return false
        if (curBlock == BlockType.PATH) return false

        // check the top-left corner
        if (y > 0 && x > 0 &&
                mBlocks[y - 1][x].walkable &&
                mBlocks[y][x - 1].walkable &&
                mBlocks[y - 1][x - 1].walkable) return false

        // check the top-right corner
        if (y > 0 && x < mWidth - 1 &&
            mBlocks[y - 1][x].walkable &&
            mBlocks[y][x + 1].walkable &&
            mBlocks[y - 1][x + 1].walkable) return false

        // check the bottom-right corner
        if (y < mHeight - 1 && x < mWidth - 1 &&
            mBlocks[y + 1][x].walkable &&
            mBlocks[y][x + 1].walkable &&
            mBlocks[y + 1][x + 1].walkable) return false

        // check the bottom-left corner
        if (y < mHeight - 1 && x > 0 &&
            mBlocks[y + 1][x].walkable &&
            mBlocks[y][x - 1].walkable &&
            mBlocks[y + 1][x - 1].walkable) return false

        // all check points are passed -> return true
        return true
    }

    /* ================================================================ */

    override fun toString(): String {
        val sBuf = StringBuffer()
        for (k in 0 until mHeight) {
            for (j in 0 until mWidth)
                sBuf.append(mBlocks[k][j].symbol)
            sBuf.append('\n')
        }
        return sBuf.toString()
    }
}
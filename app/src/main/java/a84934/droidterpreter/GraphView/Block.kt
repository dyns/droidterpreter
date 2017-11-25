package a84934.droidterpreter.GraphView

import a84934.droidterpreter.BlockVals.BlockVals
import android.graphics.Rect

enum class BlockType {
    NUM,
    ADD,
    MULT,
    MAIN
}

fun blockTypeForIndex(i : Int) : BlockType {
    if (i < 0 || i >= BlockType.values().size){
        throw IllegalArgumentException("")
    } else {
        return BlockType.values()[i]
    }
}


class Block (val blockType : BlockType,
             val width: Int, val height: Int,
             val color: Int,
             var value: BlockVals) {

    private val handleOffset = Pair(width / 2, 0)
    private val centerOut = Pair(width / 2, height)
    private val leftOut = Pair(width / 4, height)
    private val rightOut = Pair(width - (width / 4), height)

    init {

    }

    var r: Rect = Rect()
    var deltaX: Int = 0
    var deltaY: Int = 0

    fun absoluteHandleOffset() = Pair(
            (r.left + handleOffset.first).toFloat(),
            (r.top + handleOffset.second).toFloat())

    fun absoluteCenterOut() = Pair(
            (r.left + centerOut.first).toFloat(),
            (r.top + centerOut.second ).toFloat())

    fun absoluteLeftOut() = Pair(
            (r.left + leftOut.first).toFloat(),
            (r.top + leftOut.second).toFloat())

    fun absoluteRightOut() = Pair(
            (r.left + rightOut.first).toFloat(),
            (r.top + rightOut.second).toFloat())
}

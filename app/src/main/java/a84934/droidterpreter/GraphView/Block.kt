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

    var r: Rect = Rect()
    var deltaX: Int = 0
    var deltaY: Int = 0


}

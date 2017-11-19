package a84934.droidterpreter.GraphView

import a84934.droidterpreter.Block
import a84934.droidterpreter.BlockValSetActivities.SetNumValActivity
import a84934.droidterpreter.BlockVals.AddBV
import a84934.droidterpreter.BlockVals.MainBV
import a84934.droidterpreter.BlockVals.NumBV
import a84934.droidterpreter.Interpreter.Interpreter
import a84934.droidterpreter.MainActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.widget.Toast
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.ArrayList


class GController constructor(val gView: GView){

    init {
        gView.initGView(this::blockLongCLick, this)
    }

    private fun blockLongCLick(block:Block){
        showBlockOptions(block)
    }

    private fun showBlockOptions(block:Block){

        val blockV = block.value
        when (blockV){
            is AddBV -> {
                Toast.makeText(gView.context, "Select left and then right blocks", Toast.LENGTH_LONG).show()
                // tell view to wait for select 2 blocks
                gView.waitSelect(2, {
                    blockV.leftBlock = it[0]
                    blockV.rightBlock = it[1]
                })
            }
            is NumBV-> {
                val intent = Intent(gView.context, SetNumValActivity::class.java)
                intent.putExtra("i", blocks.indexOf(block)) //todo fix this
                (gView.context as MainActivity).startActivityForResult(intent, MainActivity.Companion.NUM_RESULT)
            }
            is MainBV -> {
                Toast.makeText(gView.context, "Select start block", Toast.LENGTH_LONG).show()
                gView.waitSelect(1, {
                    // get that one block and set update main
                    blockV.startBlock = it[0]
                })
            }
        }
    }

    val BLOCK_WIDTH = 130

    private val blocks: MutableList<Block> = ArrayList()

    private fun buildExpression() = buildExpressionR(blocks[0])

    private fun buildExpressionR(b : Block?) : Interpreter.Expr{
        if(b == null){
            throw IllegalArgumentException("")
        }

        val bVal = b.value

        return when(bVal){
            is MainBV -> {
                 buildExpressionR(bVal.startBlock)
            }
            is AddBV -> {
                 Interpreter.Expr.AddE(
                        buildExpressionR(bVal.leftBlock),
                buildExpressionR(bVal.rightBlock)
                )
            }
            is NumBV -> {
                 Interpreter.Expr.NumE(bVal.number)
            }
            else ->{
                // null type, should not be null
                throw NullPointerException("No block type")
            }
        }

    }

    fun existsBlockAt(p : Point) : Block?{
        val x = p.x
        val y = p.y

        var r: Rect
        for(i in (blocks.size - 1) downTo 0){
            r = blocks[i].r
            if(x >= r.left && x <= r.right && y >= r.top && y <= r.bottom){
                return blocks[i]
            }
        }
        return null
    }

    fun getBlocks() = blocks

    fun getBlock(i : Int) = blocks[i]

    fun blockCount() = blocks.size

    fun addPlus(type : Block.BlockType){

        val random = Random()

        val b = Block()

        b.type = type
        b.color =  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        b.width = BLOCK_WIDTH
        b.height = BLOCK_WIDTH
        var left = 0
        var top = 0
        b.r = Rect(left,top, left + b.width, top +  b.height)

        when (type){
            Block.BlockType.ADD -> {
                b.value = AddBV(null, null)
            }
            Block.BlockType.NUM -> {
                b.value = NumBV(0)
            }
            Block.BlockType.MAIN -> {
                b.value = MainBV(null)
            }
        }

        gView
        this.blocks.add(b)
        gView.invalidate()
    }

    fun setBlockValue(i: Int, v: Any){
        val b = blocks[i]
        b.value = v
        gView.invalidate()
    }

    fun execute(){
        val interp = Interpreter()
        var valDes = "Bad block formatting, unable to parse."

        try {
            valDes = interp.interpret(buildExpression()).toString()
        } catch (e : IllegalArgumentException){

        }

        gView.showResultDialog(valDes)
    }



}

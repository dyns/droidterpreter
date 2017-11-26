package a84934.droidterpreter.GraphView

import a84934.droidterpreter.BlockValSetActivities.SetNumValActivity
import a84934.droidterpreter.BlockVals.*
import a84934.droidterpreter.Interpreter.Interpreter
import a84934.droidterpreter.MainActivity
import a84934.droidterpreter.R
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.toast.view.*
import java.util.*
import kotlin.collections.ArrayList


class GController constructor(val gView: GView, val toastBar : LinearLayout){

    val toastCloseTV = toastBar.findViewById<TextView>(R.id.closeToastTV)
    val toastMessageTV = toastBar.findViewById<TextView>(R.id.toastTV)

    init {
        gView.initGView(this::blockLongCLick, this)

        toastCloseTV.setOnClickListener({
            hideMessageBar()
        })
    }

    public fun showMessage(text : String){
        toastBar.visibility = View.VISIBLE
        toastMessageTV.text = text
    }

    private fun hideMessageBar(){
        toastBar.visibility = View.GONE
    }

    private fun blockLongCLick(block:Block){
        showBlockOptions(block)
    }

    private fun showBlockOptions(block:Block){

        val blockV = block.value
        val f = when (blockV){
            is AddBV -> {

                {
                    val messageText = "Select left and then right blocks"
                    //Toast.makeText(gView.context, messageText, Toast.LENGTH_LONG).show()
                    showMessage(messageText)
                    // tell view to wait for select 2 blocks
                    gView.waitSelect(2, {
                        hideMessageBar()
                        blockV.leftBlock = it[0]
                        blockV.rightBlock = it[1]
                    })
                }

            }
            is NumBV-> {
                {
                    val intent = Intent(gView.context, SetNumValActivity::class.java)
                    intent.putExtra("i", blocks.indexOf(block)) //todo fix this
                    (gView.context as MainActivity).startActivityForResult(intent, MainActivity.Companion.NUM_RESULT)
                }
            }
            is MainBV -> {
                {
                    //Toast.makeText(gView.context, "Select start block", Toast.LENGTH_LONG).show()
                    showMessage("Select start block")
                    gView.waitSelect(1, {
                        hideMessageBar()
                        // get that one block and set update main
                        blockV.startBlock = it[0]
                    })
                }
            }
            is MultBV -> {
                {

                    //todo - abstract this, used for addbv too
                    //Toast.makeText(gView.context, "Select left and then right blocks", Toast.LENGTH_LONG).show()
                    // tell view to wait for select 2 blocks
                    showMessage("Select left and then right blocks")
                    gView.waitSelect(2, {
                        hideMessageBar()
                        blockV.leftBlock = it[0]
                        blockV.rightBlock = it[1]
                    })
                }
            }
        }

        f()

    }

    val BLOCK_WIDTH = 130

    private val blocks: MutableList<Block> = ArrayList()

    private fun buildExpression() = parseBlocks(blocks[0])

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

    fun addNewBlock(type : BlockType){

        val random = Random()

        val color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256))

        val newBV = when (type){
            BlockType.ADD -> AddBV(null, null)
            BlockType.MULT -> MultBV(null, null)
            BlockType.NUM ->  NumBV(0)
            BlockType.MAIN -> MainBV(null)
        }

        val b = Block(type,
                BLOCK_WIDTH,BLOCK_WIDTH,
                color,
                newBV)

//        b.type = type
//        b.color =  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
//        b.width = BLOCK_WIDTH
//        b.height = BLOCK_WIDTH
        val left = 0
        val top = 0
        b.r = Rect(left,top, left + b.width, top +  b.height)

        this.blocks.add(b)
        gView.invalidate()
    }

    fun setBlockValue(i: Int, v: BlockVals){
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

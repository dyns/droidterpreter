package a84934.droidterpreter.GraphView

import a84934.droidterpreter.BlockVals.AddBV
import a84934.droidterpreter.BlockVals.MainBV
import a84934.droidterpreter.BlockVals.MultBV
import a84934.droidterpreter.BlockVals.NumBV
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

fun gviewKotOnDraw(canvas : Canvas, p: Paint, controller : GController) {

    // clear
    canvas.drawColor(Color.WHITE);

    p.setStyle(Paint.Style.FILL);

    controller.getBlocks()

    for (b in controller.getBlocks()) {
        p.setColor(b.color);
        canvas.drawRect(b.r, p);
        p.setColor(Color.WHITE);
        p.setTextSize(75f);
        p.setStrokeWidth(10f);

        //canvas.drawRect(new Rect(b.r.left, b.r.top, b.r.left + 30, b.r.top + 30), p);

        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText("L", b.r.left, b.r.bottom, p);

        val offset = 15;
        val textLeft = b.r.left + offset;
        val textBottom = b.r.bottom - offset;

        val centerX = b.r.left + (b.width / 2);
        val centerOffset = (b.width / 4);

        val j = when(b.blockType){
            BlockType.NUM -> {
                val bv = b.value as NumBV
                val v = Integer.toString(bv.number);

                canvas.drawText(v, textLeft.toFloat(), textBottom.toFloat(), p);
            }
            BlockType.ADD -> {

                canvas.drawText("+", textLeft.toFloat(), textBottom.toFloat(), p);

                p.setColor(Color.BLACK);
                val v = b.value as AddBV;
                if(v.leftBlock != null) {
                    val toBlock = v.leftBlock!!;
                    canvas.drawLine(centerX - centerOffset.toFloat(), b.r.bottom.toFloat(), toBlock.r.left.toFloat(), toBlock.r.top.toFloat(), p);
                }
                if(v.rightBlock != null){
                    val toBlock = v.rightBlock!!;
                    canvas.drawLine(centerX + centerOffset.toFloat(), b.r.bottom.toFloat(), toBlock.r.left.toFloat(), toBlock.r.top.toFloat(), p);
                }

                Unit
            }
            BlockType.MULT -> {

                canvas.drawText("*", textLeft.toFloat(), textBottom.toFloat(), p);

                p.setColor(Color.BLACK);
                val v = b.value as MultBV
                if(v.leftBlock != null) {
                    val toBlock = v.leftBlock!!;
                    canvas.drawLine(centerX - centerOffset.toFloat(), b.r.bottom.toFloat(), toBlock.r.left.toFloat(), toBlock.r.top.toFloat(), p);
                }
                if(v.rightBlock != null){
                    val toBlock = v.rightBlock!!;
                    canvas.drawLine(centerX + centerOffset.toFloat(), b.r.bottom.toFloat(), toBlock.r.left.toFloat(), toBlock.r.top.toFloat(), p);
                }

                Unit

            }
            BlockType.MAIN -> {
                canvas.drawText("M", textLeft.toFloat(), textBottom.toFloat(), p);
                val v =  b.value as MainBV;
                if(v.startBlock != null){
                    p.setColor(Color.BLACK);
                    val toBlock = v.startBlock!!;
                    canvas.drawLine(centerX + centerOffset.toFloat(), b.r.bottom.toFloat(), toBlock.r.left.toFloat(), toBlock.r.top.toFloat(), p);
                }
                Unit
            }
        }



    }


}
package a84934.droidterpreter

import a84934.droidterpreter.BlockVals.NumBV
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private val BLOCK_TYPE_RESULT = 34

    companion object {
        val NUM_RESULT = 2
        val ADD_BLOCK_RESULT = 1
    }

    private var gView : GView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rootView = this.findViewById<LinearLayout>(R.id.mainRoot)

        gView = GView(this)

        gView!!.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )

        var addButton = this.findViewById<Button>(R.id.addButton);
        addButton.setOnClickListener({
            //gView.addPlus()
            //startActivity(Intent(this, BlockPickerActivity::class.java))
            startActivityForResult(Intent(this, BlockPickerActivity::class.java), BLOCK_TYPE_RESULT)
        });

        var executeButton = this.findViewById<Button>(R.id.executeButton);
        executeButton.setOnClickListener({
            gView!!.execute()
        })

        rootView.addView(gView)

        gView!!.addPlus(Block.BlockType.MAIN);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null){

                if(requestCode == BLOCK_TYPE_RESULT){
                    var i = data.getIntExtra("i", -1);
                    if(i != -1){
                        gView!!.addPlus(Block.fromIndex(i))
                    }
                } else if(requestCode == NUM_RESULT){
                    var v = data.getIntExtra("v", 0)
                    var i = data.getIntExtra("i", -1)
                    if(i >= 0){
                        gView!!.setBlockValue(i, NumBV(v))
                    }
                }


                /*
                else if(requestCode == ADD_BLOCK_RESULT){
                    var l = data.getIntExtra("left", -1)
                    var r = data.getIntExtra("right", -1)
                    var i = data.getIntExtra("i", -1)
                    var addBlockV = AddBlockVal()
                    addBlockV.left = l
                    addBlockV.right = r
                    gView!!.setBlockValue(i, addBlockV)
                }*/

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

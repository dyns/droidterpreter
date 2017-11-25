package a84934.droidterpreter

import a84934.droidterpreter.BlockVals.NumBV
import a84934.droidterpreter.GraphView.*
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private val BLOCK_TYPE_RESULT = 34

    companion object {
        val NUM_RESULT = 2
        val ADD_BLOCK_RESULT = 1
    }

    private var gView : GView? = null
    private var controller : GController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var guideButton = findViewById<Button>(R.id.guideButton);
        guideButton.setOnClickListener({
            startActivity(Intent(this, GuideActivity::class.java));
        })

        var rootView = this.findViewById<LinearLayout>(R.id.mainRoot)

        gView = GView(this)

        controller = GController(gView!!)

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

        var executeButton = this.findViewById<Button>(R.id.executeButton)
        executeButton.setOnClickListener({
            controller!!.execute()
        })

        rootView.addView(gView)

        controller!!.addPlus(BlockType.MAIN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(data != null){

                if(requestCode == BLOCK_TYPE_RESULT){
                    var i = data.getIntExtra("i", -1);
                    if(i != -1){
                        controller!!.addPlus(blockTypeForIndex(i))
                    }
                } else if(requestCode == NUM_RESULT){
                    var v = data.getIntExtra("v", 0)
                    var i = data.getIntExtra("i", -1)
                    if(i >= 0){
                        controller!!.setBlockValue(i, NumBV(v))
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

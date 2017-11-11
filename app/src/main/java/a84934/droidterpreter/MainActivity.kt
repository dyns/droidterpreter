package a84934.droidterpreter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rootView = this.findViewById<LinearLayout>(R.id.mainRoot)

        var gView = GView(this)

        gView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )

        var addButton = this.findViewById<Button>(R.id.addButton);
        addButton.setOnClickListener(View.OnClickListener {
            gView.addPlus()
        });

        gView.addPlus()

        rootView.addView(gView)



    }
}

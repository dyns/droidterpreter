package a84934.droidterpreter.BlockValSetActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import a84934.droidterpreter.R;

public class SetAddValActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_set_add_val);

        final int blockIndex = getIntent().getIntExtra("i", -1);

        final EditText leftEt = findViewById(R.id.leftEditText);
        final EditText rightEt = findViewById(R.id.rightEditText);

        Button done = findViewById(R.id.doneButton);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer left = parseNum(leftEt);
                Integer right = parseNum(rightEt);

                if(left != null && right != null){
                    Intent data = new Intent();
                    data.putExtra("left", left);
                    data.putExtra("right", right);
                    data.putExtra("i", blockIndex);

                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(SetAddValActivity.this, "Can't parse both nums", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Integer parseNum(EditText et){
        try {
            return Integer.valueOf(et.getText().toString().trim());
        } catch (NumberFormatException e){}
        return null;
    }

}

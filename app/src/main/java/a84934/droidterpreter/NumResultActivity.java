package a84934.droidterpreter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NumResultActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_num_result);

        final int blockIndex = getIntent().getIntExtra("i", -1);

        final EditText picker = findViewById(R.id.numPicker);
        Button done = findViewById(R.id.doneNumberButton);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = Integer.valueOf(picker.getText().toString());
                Intent data = new Intent();
                data.putExtra("v", v);
                data.putExtra("i", blockIndex);
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }

}

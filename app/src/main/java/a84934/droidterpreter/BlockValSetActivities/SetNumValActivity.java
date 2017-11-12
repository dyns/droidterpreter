package a84934.droidterpreter.BlockValSetActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import a84934.droidterpreter.R;

public class SetNumValActivity extends AppCompatActivity {

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

                Integer v = null;
                String text = picker.getText().toString();

                try {
                    v = Integer.valueOf(text);
                } catch (NumberFormatException e){}

                if(v != null){
                    Intent data = new Intent();
                    data.putExtra("v", v);
                    data.putExtra("i", blockIndex);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(SetNumValActivity.this, "Invalid number: " + text, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

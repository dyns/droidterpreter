package a84934.droidterpreter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BlockPickerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_picker);

        RecyclerView recyclerView = findViewById(R.id.blockRecyclerView);

        final BlockTypeAdapter adapter = new BlockTypeAdapter(new BlockTypeAdapter.OnClickListener() {
            @Override
            public void onBlockTypeClicked(int i) {
                Toast.makeText(BlockPickerActivity.this, "" + i, Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("i", i);
                BlockPickerActivity.this.setResult(RESULT_OK, data);
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}

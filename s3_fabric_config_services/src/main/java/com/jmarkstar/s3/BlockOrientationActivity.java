package com.jmarkstar.s3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class BlockOrientationActivity extends AppCompatActivity {

    private TextView mTvText;
    private List<String> names;

    public static void start(Context context) {
        Intent starter = new Intent(context, BlockOrientationActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_orientation);
        names = new ArrayList<>();
        mTvText = findViewById(R.id.tv_text);
    }

    public void onAddName(View view){
        names.add("Name "+(names.size()+1));
        StringBuilder namesb = new StringBuilder();
        for(String name : names){
            namesb.append(name);
            namesb.append("\n");
        }
        mTvText.setText(namesb.toString());
    }
}

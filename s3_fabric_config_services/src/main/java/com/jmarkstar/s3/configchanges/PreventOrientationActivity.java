package com.jmarkstar.s3.configchanges;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jmarkstar.s3.R;

import java.util.ArrayList;

public class PreventOrientationActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, PreventOrientationActivity.class);
        context.startActivity(starter);
    }

    private TextView mTvText;
    private ArrayList<String> names;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevent_orientation);
        names = new ArrayList<>();
        mTvText = findViewById(R.id.tv_text);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
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

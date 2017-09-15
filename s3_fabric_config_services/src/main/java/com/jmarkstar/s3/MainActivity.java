package com.jmarkstar.s3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onGoBlockOrientation(View view){
        BlockOrientationActivity.start(this);
    }

    public void onGoPreventOrientation(View view){
        PreventOrientationActivity.start(this);
    }

    public void onGoSaveStates(View view){
        SaveStateActivity.start(this);
    }

    public void onGoThreads(View view){
    }

    public void onGoWorkFragments(View view){

    }
}

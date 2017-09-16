package com.jmarkstar.s3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jmarkstar.s3.configchanges.BackgroundTaskStateActivity;
import com.jmarkstar.s3.configchanges.BlockOrientationActivity;
import com.jmarkstar.s3.configchanges.PreventOrientationActivity;
import com.jmarkstar.s3.configchanges.SaveComplexStateActivity;
import com.jmarkstar.s3.configchanges.SaveStateActivity;
import com.jmarkstar.s3.services.MyService;

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

    public void onGoSaveComplexData(View view){
        SaveComplexStateActivity.start(this);
    }

    public void onExecThread(View view){
        BackgroundTaskStateActivity.start(this);
    }

    public void onExecService(View view){
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("param1", "value1");
        startService(intent);
    }
}

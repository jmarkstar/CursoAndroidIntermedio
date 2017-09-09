package com.jmarkstar.sesion2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by jmarkstar on 9/8/17.
 */
public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGoStartActivityForResult(View view){
        ActForResultInitActivity.start(this);
    }

    public void onGoProximity(View view){
        ProximityActivity.start(this);
    }

    public void onGoAcelerometer(View view){
        AccelerometerActivity.start(this);
    }

    public void onGoGyroscope(View view){
        GyroscopeActivity.start(this);
    }

    public void onGoTakePhoto(View view){
        TakePictureActivity.start(this);
    }

    public void onGoRecordVideo(View view){
        RecordVideoActivity.start(this);
    }

    public void onGoCamera(View view){
        CameraViewActivity.start(this);
    }
}

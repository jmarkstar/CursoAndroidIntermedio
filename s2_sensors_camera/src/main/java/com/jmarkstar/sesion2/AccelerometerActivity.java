package com.jmarkstar.sesion2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "AccelerometerAct";

    private TextView mTvData;

    private SensorManager mSensorManager;//permite acceder a los sensores del dispositivo.
    private Sensor mAccelerometerSensor;


    public static void start(Context context) {
        Intent starter = new Intent(context, AccelerometerActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        mTvData = (TextView)findViewById(R.id.tv_data);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Usar este filtro o el <uses-feature/> para que google play lo filtre.
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            Log.v(TAG, "Si hay acelerometro");
        }else{
            Log.v(TAG, "No hay acelerometro");
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if(mSensorManager!=null)
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override protected void onPause() {
        super.onPause();
        if(mSensorManager!=null)
            mSensorManager.unregisterListener(this);
    }

    @Override public void onSensorChanged(SensorEvent sensorEvent) {
        mTvData.setText("X: "+ sensorEvent.values[0]+" \n Y: "+sensorEvent.values[1]+" \n Z: "+sensorEvent.values[2]);
    }

    @Override public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

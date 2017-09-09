package com.jmarkstar.sesion2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Determina la velocidad angular del dispositivo en cualquier instante dado; Nos dice que tan rapido
 * el dispositivo esta rotando entre sus ejes X,Y y Z.
 */
public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private TextView mTvData;

    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;

    public static void start(Context context) {
        Intent starter = new Intent(context, GyroscopeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        mTvData = (TextView)findViewById(R.id.tv_data);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        mTvData.setText("X: "+x+" rad/s \n Y: "+y+" rad/s \n Z: "+z+" rad/s");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

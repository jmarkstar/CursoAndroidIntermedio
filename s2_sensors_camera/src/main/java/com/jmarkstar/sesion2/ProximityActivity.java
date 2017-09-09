package com.jmarkstar.sesion2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

/** Proximity, max range is 5 cm.
 * */
public class ProximityActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ProximityActivity";

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private RelativeLayout mRlRoot;

    public static void start(Context context) {
        Intent starter = new Intent(context, ProximityActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        mRlRoot = (RelativeLayout)findViewById(R.id.rl_root);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(proximitySensor == null) {
            Toast.makeText(this, "El sensor de proximidad no esta disponible en este celular", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, 2 * 1000 * 1000);//microsegundos
    }

    @Override protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v(TAG, "Proximity onSensorChanged()");
        if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
            mRlRoot.setBackgroundColor(Color.BLACK);
        } else {
            mRlRoot.setBackgroundColor(Color.GREEN);
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

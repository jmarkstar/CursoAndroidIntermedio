package com.jmarkstar.sesion1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/** Sesion 1 MENU
 * Created by jmarkstar on 01/09/2017.
 */
public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGoLocation1(View view){
        Intent intent = new Intent(this, GoogleLocationApiActivity.class);
        startActivity(intent);
    }

    public void onGoLocation2(View view){
        Intent intent = new Intent(this, GoogleLocationApi2Activity.class);
        startActivity(intent);
    }

    public void onGoMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onGoMapsWithLocation(View view){
        Intent intent = new Intent(this, MapsWithLocationActivity.class);
        startActivity(intent);
    }
}

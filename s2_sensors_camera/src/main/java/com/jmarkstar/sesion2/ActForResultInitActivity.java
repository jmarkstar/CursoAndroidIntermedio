package com.jmarkstar.sesion2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by jmarkstar on 9/8/17.
 */
public class ActForResultInitActivity extends AppCompatActivity {

    private static final String TAG = "ActForResultInit";

    private static final int REQUEST_CODE = 1000;

    public static void start(Context context) {
        Intent starter = new Intent(context, ActForResultInitActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_for_result_init);
    }

    public void onCallOtherActivity(View view){
        Intent intent = new Intent(this, ActForResultCalledActivity.class);
        intent.putExtra("param0", "param 0 value");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String result1 = data.getStringExtra("result");
                Log.v(TAG, "result = "+result1);
            }else if(resultCode == 2000){
                String result1 = data.getStringExtra("result_2000");
                Log.v(TAG, "result 2000 = "+result1);
            }else if(resultCode == RESULT_CANCELED){
                Log.v(TAG, "Se ha cancelado");
            }
        }
    }
}

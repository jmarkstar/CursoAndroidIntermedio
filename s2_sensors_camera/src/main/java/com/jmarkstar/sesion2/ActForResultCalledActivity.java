package com.jmarkstar.sesion2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by jmarkstar on 9/8/17.
 */
public class ActForResultCalledActivity extends AppCompatActivity {

    private static final String TAG = "ActForResultCalled";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_for_result_called);

        String param0 = getIntent().getStringExtra("param0");
        Log.v(TAG, "param0 = "+param0);

    }

    public void onReturnWithResult1(View view){
        Intent intentResult = new Intent();
        intentResult.putExtra("result", "resultado 1");
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public void onReturnWithResult2(View view){
        Intent intentResult = new Intent();
        intentResult.putExtra("result", "resultado 2");
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public void onReturnWithError(View view){
        Intent intentResult = new Intent();
        intentResult.putExtra("result_2000", "resultado de 2000");
        setResult(2000, intentResult);
        finish();
    }

    @Override public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}

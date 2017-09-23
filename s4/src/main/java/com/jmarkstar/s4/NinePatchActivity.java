package com.jmarkstar.s4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class NinePatchActivity extends AppCompatActivity {

    private LinearLayout llNinePatch;

    public static void start(Context context) {
        Intent starter = new Intent(context, NinePatchActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_patch);
        llNinePatch = findViewById(R.id.ll_ninepatch);
    }

    public void onShowNinePatchItems(View view){
        llNinePatch.setVisibility(View.VISIBLE);
    }
}

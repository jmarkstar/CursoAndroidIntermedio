package com.jmarkstar.s3.configchanges;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jmarkstar.s3.R;

import java.util.ArrayList;

public class SaveStateActivity extends AppCompatActivity {

    private TextView mTvText;
    private ArrayList<String> names;

    public static void start(Context context) {
        Intent starter = new Intent(context, SaveStateActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_orientation);
        names = new ArrayList<>();
        mTvText = findViewById(R.id.tv_text);
    }

    /** https://developer.android.com/reference/android/os/TransactionTooLargeException.html
     * hay que tener cuidado con guardar datos de gran tamaño, ya que este buffer soporta un mega
     * como maximo, sino ocurrirá el error TransactionTooLargeException.
    * */
    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("names", names);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        names = savedInstanceState.getStringArrayList("names");
        showItems();
    }

    public void onAddName(View view){
        names.add("Name "+(names.size()+1));
        showItems();
    }

    private void showItems(){
        StringBuilder namesb = new StringBuilder();
        for(String name : names){
            namesb.append(name);
            namesb.append("\n");
        }
        mTvText.setText(namesb.toString());
    }
}

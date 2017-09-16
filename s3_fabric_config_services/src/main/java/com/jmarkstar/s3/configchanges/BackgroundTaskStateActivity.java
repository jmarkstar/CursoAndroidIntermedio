package com.jmarkstar.s3.configchanges;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jmarkstar.s3.R;

public class BackgroundTaskStateActivity extends AppCompatActivity implements TaskRetainedFragment.TaskCallbacks{

    private static final String TAG = "BackgroundTaskState";

    private static final String KEY_CURRENT_PROGRESS = "current_progress";
    private static final String KEY_PERCENT_PROGRESS = "percent_progress";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskRetainedFragment mTaskFragment;

    private ProgressBar mProgressBar;
    private TextView mPercent;
    private Button mButton;

    public static void start(Context context) {
        Intent starter = new Intent(context, BackgroundTaskStateActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_task_state);

        //Iniciar los views.
        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        mPercent = (TextView) findViewById(R.id.percent_progress);
        mButton = (Button) findViewById(R.id.task_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (mTaskFragment.isRunning()) {
                    mTaskFragment.cancel();
                } else {
                    mTaskFragment.start();
                }
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (TaskRetainedFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (mTaskFragment == null) {
            mTaskFragment = new TaskRetainedFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (mTaskFragment.isRunning()) {
            mButton.setText(getString(R.string.cancel));
        } else {
            mButton.setText(getString(R.string.start));
        }
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restaurar el estado guardado.
        if (savedInstanceState != null) {
            mProgressBar.setProgress(savedInstanceState.getInt(KEY_CURRENT_PROGRESS));
            mPercent.setText(savedInstanceState.getString(KEY_PERCENT_PROGRESS));
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //guardar el estado antes que se destruya el activity.
        outState.putInt(KEY_CURRENT_PROGRESS, mProgressBar.getProgress());
        outState.putString(KEY_PERCENT_PROGRESS, mPercent.getText().toString());
    }

    @Override public void onPreExecute() {
        mButton.setText(getString(R.string.cancel));
        Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void onProgressUpdate(int percent) {
        mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
        mPercent.setText(percent + "%");
    }

    @Override public void onCancelled() {
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(0);
        mPercent.setText(getString(R.string.zero_percent));
        Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void onPostExecute() {
        mButton.setText(getString(R.string.start));
        mProgressBar.setProgress(mProgressBar.getMax());
        mPercent.setText(getString(R.string.one_hundred_percent));
        Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }
}

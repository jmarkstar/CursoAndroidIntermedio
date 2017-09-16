package com.jmarkstar.s3;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

/**
 * Created by jmarkstar on 9/15/17.
 */

public class TaskRetainedFragment extends Fragment {

    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private DummyTask mTask;
    private boolean mRunning;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof TaskCallbacks)) {
            throw new IllegalStateException("Activity debe implementar la interface TaskCallbacks.");
        }
        mCallbacks = (TaskCallbacks) context;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        cancel();
    }

    /**
     * Inicia la tarea.
     */
    public void start() {
        if (!mRunning) {
            mTask = new DummyTask();
            mTask.execute();
            mRunning = true;
        }
    }

    /**
     * Cancela la tarea.
     */
    public void cancel() {
        if (mRunning) {
            mTask.cancel(false);
            mTask = null;
            mRunning = false;
        }
    }

    /**
     * Retornar el estado de la tarea.
     */
    public boolean isRunning() {
        return mRunning;
    }

    private class DummyTask extends AsyncTask<Void, Integer, Void> {

        @Override protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
            mRunning = true;
        }

        @Override protected Void doInBackground(Void... voids) {
            for (int i = 0; !isCancelled() && i < 100; i++) {
                SystemClock.sleep(100);
                publishProgress(i);
            }
            return null;
        }

        @Override protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(percent[0]);
            }
        }

        @Override protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
            mRunning = false;
        }

        @Override protected void onPostExecute(Void ignore) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
            mRunning = false;
        }
    }
}

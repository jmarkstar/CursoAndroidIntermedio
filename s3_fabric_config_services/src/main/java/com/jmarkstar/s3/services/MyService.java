package com.jmarkstar.s3.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jmarkstar on 16/09/2017.
 */
public class MyService extends Service {

    private static final String TAG = "MyService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Aqui escribimos el cuerpo de service
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int x=0;x<10000000;x++){
                    Log.v(TAG, "Log desde un service");
                }
            }
        }).start();
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //retornamos un IBinder para la comunicacion con otros componentes conectados.
        return null;
    }
}

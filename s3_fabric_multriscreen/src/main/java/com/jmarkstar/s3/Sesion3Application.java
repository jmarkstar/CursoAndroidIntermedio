package com.jmarkstar.s3;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by jmarkstar on 14/09/2017.
 */
public class Sesion3Application extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}

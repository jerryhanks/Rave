package com.flutterwave.raveApp;

import android.app.Application;

import com.flutterwave.rave.Rave;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Rave.initEnvironment(this,Rave.ENV_TESTING);
    }
}

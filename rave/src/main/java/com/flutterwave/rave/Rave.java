package com.flutterwave.rave;

import android.app.Application;
import android.util.Log;

import java.io.File;

/**
 * Created by @Po10cio on 22/03/2017 for Rave.
 */

public final class Rave {
    public static final String ENV_TESTING = "com.flutterwave.rave.ENV_TESTING";
    public static final String ENV_PRODUCTION = "com.flutterwave.rave.ENV_PRODUCTION";

    private static boolean isProduction = false;
    private static Application mApp = null;

    public static synchronized void initEnvironment(Application mainApp, String env) {
        mApp = mainApp;
        switch (env) {
            case ENV_PRODUCTION:
                isProduction = true;
                break;
            case ENV_TESTING:
                isProduction = false;
                break;
        }
    }


    public static String getBaseUrl() {
        checkIfRaveIsInitialized();
        Log.d("POT", "IsProduction: " + isProduction);

        if (isProduction()) {
            return "https://api.ravepay.co/";
        } else {
//            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/flwv3-pug/getpaidx/api/";
            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/";
        }
    }

    private static void checkIfRaveIsInitialized() {
        if (mApp == null) {
            throw new RuntimeException("Rave Dialog not initializsed");
        }
    }

    public static boolean isProduction() {
        checkIfRaveIsInitialized();
        return isProduction;
    }


    public static File getCacheDir() {
        checkIfRaveIsInitialized();
        return mApp.getExternalCacheDir();
    }
}

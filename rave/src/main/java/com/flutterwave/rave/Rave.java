package com.flutterwave.rave;

import android.util.Log;

/**
 * Created by @Po10cio on 22/03/2017 for Rave.
 */

public final class Rave {
    public static final String ENV_TESTING = "com.flutterwave.rave.ENV_TESTING";
    public static final String ENV_PRODUCTION = "com.flutterwave.rave.ENV_PRODUCTION";

    private static boolean isProduction = false;

    public static synchronized void initEnvironment(String env) {
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
        Log.d("POT", "IsProduction: " + isProduction);

        if (isProduction()) {
            return "https://api.ravepay.co/";
        } else {
//            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/flwv3-pug/getpaidx/api/";
            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/";
        }
    }

    public static boolean isProduction() {
        return isProduction;
    }
}

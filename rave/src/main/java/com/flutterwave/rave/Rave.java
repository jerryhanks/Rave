package com.flutterwave.rave;

/**
 * Created by @Po10cio on 22/03/2017 for Rave.
 */

public class Rave {
    public static String BASE_URL;
    private static boolean isProduction = false;

    public static void initialise(boolean isProd) {
        isProduction = isProd;
    }

    public static boolean isProduction() {
        return isProduction;
    }

    protected static String getBaseUrl() {
        if (isProduction) {
            return "https://api.ravepay.co/";
        } else {
//            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/flwv3-pug/getpaidx/api/";
            return "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/";
        }
    }
}

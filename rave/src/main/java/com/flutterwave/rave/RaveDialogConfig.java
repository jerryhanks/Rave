package com.flutterwave.rave;

/**
 * Created by @Po10cio on 22/03/2017 for Rave.
 */

public class RaveDialogConfig {
    public static String BASE_URL = "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/flwv3-pug/getpaidx/api";
    private static boolean isProduction = false;

    public static void initialise(boolean isProd) {
        isProduction = isProd;
        if (isProduction) {
            BASE_URL = "https://api.ravepay.co/flwv3-pug/getpaidx/api";
        } else {
            BASE_URL = "http://flw-pms-dev.eu-west-1.elasticbeanstalk.com/flwv3-pug/getpaidx/api";
        }

    }

    public static boolean isProduction() {
        return isProduction;
    }
}

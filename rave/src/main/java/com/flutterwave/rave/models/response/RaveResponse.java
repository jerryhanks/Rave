package com.flutterwave.rave.models.response;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public abstract class RaveResponse {
    String status;
    String message;


    RaveResponse() {
    }

    public String getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }

}

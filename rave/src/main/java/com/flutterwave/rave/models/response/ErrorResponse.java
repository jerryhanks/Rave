package com.flutterwave.rave.models.response;

import com.flutterwave.rave.models.data.ErrorData;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class ErrorResponse extends RaveResponse {
    @SerializedName("data")
    private ErrorData data;

    private ErrorResponse(){}

    public ErrorData getData() {
        return data;
    }
}

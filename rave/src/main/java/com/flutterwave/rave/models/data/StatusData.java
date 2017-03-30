package com.flutterwave.rave.models.data;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */
public class StatusData {

    @SerializedName("responsecode")
    private String responseCode;

    @SerializedName("responsemessage")
    private String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("StatusData")
                .add("Response Code", responseCode)
                .add("Response Message", responseMessage)
                .toString();
    }
}
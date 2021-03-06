package com.flutterwave.rave.models.response;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class AccountValidateResponse extends RaveResponse {
    @SerializedName("data")
    private ResponseData data;

    private AccountValidateResponse() {
    }


    public ResponseData getData() {
        return data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CardValidateResponse")
                .add("Status", status)
                .add("Message", message)
                .add("Data", data)
                .toString();
    }
}

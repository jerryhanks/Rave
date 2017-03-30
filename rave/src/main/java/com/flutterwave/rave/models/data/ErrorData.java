package com.flutterwave.rave.models.data;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class ErrorData {
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    private ErrorData() {
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ErrorData")
                .add("Code ", code)
                .add("Message ", message)
                .toString();
    }
}

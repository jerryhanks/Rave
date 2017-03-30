package com.flutterwave.rave.models.request;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Shittu on 19/12/2016.
 */

public class ShortCodeRequestData extends BaseRequestData {

    @SerializedName("cvv")
    private String cvv;

    @SerializedName("shotcode")
    private String shortCode;

    @SerializedName("pin")
    private String pin;

    public ShortCodeRequestData(String PBFPubKey, String amount, String email, String IP,
                                String txRef, String country, String currency, String firstname,
                                String lastName, String narration, List<Map<String, Object>> meta,
                                String cvv, String shortCode, String pin) {
        super(PBFPubKey, amount, email, IP, txRef, country, currency, firstname, lastName, narration, meta);
        this.cvv = cvv;
        this.shortCode = shortCode;
        this.pin = pin;
    }

    public String getcvv() {
        return cvv;
    }

    public String getshortcode() {
        return shortCode;
    }

    public String getpin() {
        return pin;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cvv", cvv)
                .add("shortCode", shortCode)
                .add("pin", pin)
                .toString();
    }
}

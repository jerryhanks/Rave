package com.flutterwave.rave.models.request;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Shittu on 19/12/2016.
 */

public class CardChargeRequestData extends BaseRequestData {

    @SerializedName("cardno")
    private String cardNo;

    @SerializedName("cvv")
    private String cvv;

    @SerializedName("expiryyear")
    private String expiryYear;

    @SerializedName("expirymonth")
    private String expiryMonth;

    @SerializedName("pin")
    private String pin;

    public CardChargeRequestData(String PBFPubKey, String amount, String email, String IP,
                                 String txRef, String country, String currency, String firstName,
                                 String lastName, String narration, List<Map<String, Object>> meta,
                                 String cardNo, String cvv, String expiryYear, String expiryMonth, String pin) {
        super(PBFPubKey, amount, email, IP, txRef, country, currency, firstName, lastName, narration, meta);
        this.cardNo = cardNo;
        this.cvv = cvv;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
        this.pin = pin;
    }

    public String getcardno() {
        return cardNo;
    }

    public String getcvv() {
        return cvv;
    }

    public String getexpiryyear() {
        return expiryYear;
    }

    public String getexpirymonth() {
        return expiryMonth;
    }

    public String getpasscode() {
        return pin;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cardNo", cardNo)
                .add("cvv", cvv)
                .add("expiryYear", expiryYear)
                .add("expiryMonth", expiryMonth)
                .add("pin", pin)
                .toString();
    }
}

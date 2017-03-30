package com.flutterwave.rave.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class AccountValidateRequest {
    @SerializedName("PBFPubKey")
    private String PBFPubKey;

    @SerializedName("transactionreference")
    private String transactionRef;

    private String otp;

    public AccountValidateRequest(String pbfPubKey, String transactionRef, String otp) {
        PBFPubKey = pbfPubKey;
        this.transactionRef = transactionRef;
        this.otp = otp;
    }

    public void setPBFPubKey(String PBFPubKey) {
        this.PBFPubKey = PBFPubKey;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

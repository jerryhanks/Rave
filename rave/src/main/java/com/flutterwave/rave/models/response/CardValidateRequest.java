package com.flutterwave.rave.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class CardValidateRequest {
    @SerializedName("PBFPubKey")
    private String PBFPubKey;

    @SerializedName("transaction_reference")
    private String transactionRef;

    private String otp;


    public CardValidateRequest(String pbfPubKey, String trsactionRef, String otp) {
        PBFPubKey = pbfPubKey;
        this.transactionRef = trsactionRef;
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

package com.flutterwave.rave.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class ChargeRequest {
    @SerializedName("PBFPubKey")
    private String PBFPubKey;
    private String client;
    private String alg;

    public ChargeRequest(String pbfPubKey, String client, String alg) {
        PBFPubKey = pbfPubKey;
        this.client = client;
        this.alg = alg;
    }

    public void setPBFPubKey(String PBFPubKey) {
        this.PBFPubKey = PBFPubKey;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}

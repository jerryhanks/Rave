package com.flutterwave.rave.models.response;

import com.flutterwave.rave.models.data.ChargeToken;
import com.flutterwave.rave.models.data.Customer;
import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class ResponseData {
    private long id;

    @SerializedName("txRef")
    private String txRef;

    @SerializedName("flwRef")
    private String flwRef;

    private double amount;
    private double chargedAmount;
    private String narration;
    private String status;

    @SerializedName("appfee")
    private double appFee;

    @SerializedName("merchantfee")
    private double merchantFee;

    @SerializedName("merchantbearsfee")
    private int merchantBearsFee;

    @SerializedName("chargeResponseCode")
    private String chargeResponseCode;

    @SerializedName("chargeResponseMessage")
    private String chargeResponseMessage;

    @SerializedName("authModelUsed")
    private String authModelUsed;

    @SerializedName("vbvrespmessage")
    private String vbvRespMessage;

    @SerializedName("authurl")
    private String authUrl;

    @SerializedName("vbvrespcode")
    private String vbvRespcode;

    @SerializedName("acctvalrespmsg")
    private String acctvalRespMsg;

    @SerializedName("acctvalrespcode")
    private String acctvalRespCode;

    private Customer customer;

    @SerializedName("chargeToken")
    private ChargeToken chargeToken;

    private ResponseData() {
    }


    public long getId() {
        return id;
    }

    public String getTxRef() {
        return txRef;
    }

    public String getFlwRef() {
        return flwRef;
    }

    public double getAmount() {
        return amount;
    }

    public double getChargedAmount() {
        return chargedAmount;
    }

    public String getNarration() {
        return narration;
    }

    public String getStatus() {
        return status;
    }

    public String getVbvRespMessage() {
        return vbvRespMessage;
    }

    public String getVbvRespcode() {
        return vbvRespcode;
    }

    public String getAcctvalRespMsg() {
        return acctvalRespMsg;
    }

    public String getAcctvalRespCode() {
        return acctvalRespCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ChargeToken getChargeToken() {
        return chargeToken;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ChargeResponseData: ")
                .add("Id", id)
                .add("TexRef", txRef)
                .add("FleRef", flwRef)
                .add("Amount", amount)
                .add("Charged Amount", chargedAmount)
                .add("Narration", narration)
                .add("Status", status)
                .add("VBVRespMessage", vbvRespMessage)
                .add("VBVResCode", vbvRespcode)
                .add("AcctValRespMess", acctvalRespMsg)
                .add("AcctValRespCode", acctvalRespCode)
                .add("Customer", customer)
                .add("ChargeToken", chargeToken)
                .toString();
    }

    public double getAppFee() {
        return appFee;
    }

    public double getMerchantFee() {
        return merchantFee;
    }

    public int getMerchantBearsFee() {
        return merchantBearsFee;
    }

    public String getAuthModelUsed() {
        return authModelUsed;
    }

    public String getChargeResponseCode() {
        return chargeResponseCode;
    }

    public String getChargeResponseMessage() {
        return chargeResponseMessage;
    }

    public String getAuthUrl() {
        return authUrl;
    }
}

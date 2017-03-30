package com.flutterwave.rave.models.request;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Shittu on 19/12/2016.
 */

public class AccountChargeRequestData extends BaseRequestData {

    @SerializedName("accountnumber")
    private String accountNumber;

    @SerializedName("accountbank")
    private String accountBank;

    @SerializedName("validateoption")
    private String validateOption;

    @SerializedName("payment_type")
    private String paymentType;


    public AccountChargeRequestData(String PBFPubKey, String amount, String email, String IP,
                                    String txRef, String country, String currency, String firstName,
                                    String lastName, String narration, List<Map<String, Object>> meta,
                                    String accountNumber, String accountBank, String validateOption) {
        super(PBFPubKey, amount, email, IP, txRef, country, currency, firstName, lastName, narration, meta);
        this.accountNumber = accountNumber;
        this.accountBank = accountBank;
        this.validateOption = validateOption;
        this.paymentType = "account";
    }

    public String getaccountnumber() {
        return accountNumber;
    }

    public String getaccountbank() {
        return accountBank;
    }

    public String getpayment_type() {
        return paymentType;
    }

    public String getvalidateoption() {
        return validateOption;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountNumber", accountNumber)
                .add("accountBank", accountBank)
                .add("validateOption", validateOption)
                .add("paymentType", paymentType)
                .toString();
    }
}

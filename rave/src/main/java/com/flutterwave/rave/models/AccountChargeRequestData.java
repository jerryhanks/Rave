package com.flutterwave.rave.models;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Map;

/**
 * Created by Shittu on 19/12/2016.
 */

public class AccountChargeRequestData extends BaseRequestData {

    private String accountNumber;
    private String accountBank;
    private String validateOption;
    private String payment_type;


    public AccountChargeRequestData(String PBFPubKey, String amount, String email, String IP,
                                    String txRef, String country, String currency, String firstname,
                                    String lastname, String narration, List<Map<String, Object>> meta,
                                    String accountnumber, String accountbank, String validateoption) {
        super(PBFPubKey, amount, email, IP, txRef, country, currency, firstname, lastname, narration, meta);
        this.accountNumber = accountnumber;
        this.accountBank = accountbank;
        this.validateOption = validateoption;
        this.payment_type = "account";
    }

    public String getaccountnumber() {
        return accountNumber;
    }

    public String getaccountbank() {
        return accountBank;
    }

    public String getpayment_type() {
        return payment_type;
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
                .add("payment_type", payment_type)
                .toString();
    }
}

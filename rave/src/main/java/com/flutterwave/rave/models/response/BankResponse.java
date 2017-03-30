package com.flutterwave.rave.models.response;

import com.flutterwave.rave.models.data.Bank;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class BankResponse extends RaveResponse {
    private List<Bank> data;

    protected BankResponse() {
    }

    public List<Bank> getData() {
        return data;
    }

    public void setData(List<Bank> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Banks")
                .add("Status", status)
                .add("Message", message)
                .add("Banks", getData().size())
                .toString();
    }
}

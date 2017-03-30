package com.flutterwave.rave.models.data;

import com.google.common.base.MoreObjects;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class Bank {
    private String name;
    private String code;

    public Bank(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Bank")
                .add("Name", name)
                .add("Code", code)
                .toString();
    }
}

package com.flutterwave.rave.models.data;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class Customer {
    private long id;
    private String phone;
    private String fullName;
    private String email;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    @SerializedName("AccountId")
    private String accountId;

    private Customer() {
    }

    public long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Customer: ")
                .add("Id", id)
                .add("Phone", phone)
                .add("FullName", fullName)
                .add("Email", email)
                .add("created At", createdAt)
                .add("Updated At", updatedAt)
                .add("deleted At", deletedAt)
                .add("Account Id", accountId)
                .toString();
    }
}

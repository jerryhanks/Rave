package com.flutterwave.rave.models.data;

import com.google.common.base.MoreObjects;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class ChargeToken {
    private String useToken;
    private String embedToken;

    private ChargeToken() {
    }

    public String getUseToken() {
        return useToken;
    }

    public String getEmbedToken() {
        return embedToken;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Charge Token")
                .add("Use Token", useToken)
                .add("Embed Token", embedToken)
                .toString();
    }
}

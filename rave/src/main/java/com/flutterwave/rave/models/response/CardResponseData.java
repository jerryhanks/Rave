package com.flutterwave.rave.models.response;

import com.flutterwave.rave.models.data.StatusData;
import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class CardResponseData {

    @SerializedName("data")
    private StatusData statusData;

    @SerializedName("tx")
    private ResponseData content;

    private CardResponseData() {
    }

    public ResponseData getContent() {
        return content;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CardResponseData")
                .add("StatusData", statusData)
                .add("Content", content)
                .toString();
    }

    public StatusData getStatusData() {
        return statusData;
    }


}

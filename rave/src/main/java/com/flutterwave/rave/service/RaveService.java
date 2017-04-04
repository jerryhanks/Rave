package com.flutterwave.rave.service;

import android.support.annotation.NonNull;

import com.flutterwave.rave.models.response.AccountValidateRequest;
import com.flutterwave.rave.models.response.AccountValidateResponse;
import com.flutterwave.rave.models.response.BankResponse;
import com.flutterwave.rave.models.response.CardValidateRequest;
import com.flutterwave.rave.models.response.CardValidateResponse;
import com.flutterwave.rave.models.response.ChargeRequest;
import com.flutterwave.rave.models.response.ChargeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public interface RaveService {
    /**
     * Lists all the banks available and open for transaction
     */
    @Headers("Cache-Control: max-age=86400,max-stale=86400")
    @GET("banks")
    Call<BankResponse> getBanks();


    /**
     * Charge Endpoint
     */
    @POST("flwv3-pug/getpaidx/api/charge")
    Call<ChargeResponse> charge(@Body @NonNull ChargeRequest chargeRequest);

    /**
     * Validate Card Charge only
     */
    @POST("flwv3-pug/getpaidx/api/validatecharge")
    Call<CardValidateResponse> validateCardCharge(@Body @NonNull CardValidateRequest cardValidateRequest);

    /**
     * Validate Account Charge only
     */
    @POST("flwv3-pug/getpaidx/api/validate")
    Call<AccountValidateResponse> validateAccountCharge(@Body @NonNull AccountValidateRequest accountValidateRequest);
}

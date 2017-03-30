package com.flutterwave.rave.service;

import android.support.annotation.NonNull;

import com.flutterwave.rave.ServiceGenerator;
import com.flutterwave.rave.data.RaveData;
import com.flutterwave.rave.models.response.AccountValidateRequest;
import com.flutterwave.rave.models.response.AccountValidateResponse;
import com.flutterwave.rave.models.response.BankResponse;
import com.flutterwave.rave.models.response.CardValidateRequest;
import com.flutterwave.rave.models.response.CardValidateResponse;
import com.flutterwave.rave.models.response.ChargeRequest;
import com.flutterwave.rave.models.response.ChargeResponse;
import com.flutterwave.rave.models.response.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class RaveClient {
    private static RaveService mService = ServiceGenerator.createService(RaveService.class);

    public static void getBanks(@NonNull final RaveData.OnBankCallback callback) {
        mService.getBanks()
                .enqueue(new Callback<BankResponse>() {
                    @Override
                    public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onBanks(response.body().getData());
                        } else {
                            try {
                                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                callback.onError(errorResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BankResponse> call, Throwable t) {
                        callback.onFailure(t);

                    }
                });
    }

    public static void charge(@NonNull ChargeRequest chargeRequest, @NonNull final RaveData.OnChargeResponse callback) {
        mService.charge(chargeRequest)
                .enqueue(new Callback<ChargeResponse>() {
                    @Override
                    public void onResponse(Call<ChargeResponse> call, Response<ChargeResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onCharge(response.body());
                        } else {

                            try {
                                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                callback.onError(errorResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChargeResponse> call, Throwable t) {

                        callback.onFailure(t);
                    }
                });
    }


    public static void validateCardCharge(@NonNull CardValidateRequest request, @NonNull final RaveData.OnCardValidateCallback callback) {
        mService.validateCardCharge(request)
                .enqueue(new Callback<CardValidateResponse>() {
                    @Override
                    public void onResponse(Call<CardValidateResponse> call, Response<CardValidateResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onCardValidate(response.body());
                        } else {
                            try {
                                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                callback.onError(errorResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CardValidateResponse> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });

    }

    public static void validateAccountCharge(@NonNull AccountValidateRequest request, @NonNull final RaveData.OnAccountValidateCallback callback) {
        mService.validateAccountCharge(request)
                .enqueue(new Callback<AccountValidateResponse>() {
                    @Override
                    public void onResponse(Call<AccountValidateResponse> call, Response<AccountValidateResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onAccountValidate(response.body());
                        } else {
                            try {
                                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                callback.onError(errorResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountValidateResponse> call, Throwable t) {
                        callback.onFailure(t);

                    }
                });
    }

}

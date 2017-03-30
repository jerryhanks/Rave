package com.flutterwave.rave.data;

import android.support.annotation.NonNull;

import com.flutterwave.rave.models.response.AccountValidateResponse;
import com.flutterwave.rave.models.data.Bank;
import com.flutterwave.rave.models.response.CardValidateResponse;
import com.flutterwave.rave.models.response.ChargeResponse;
import com.flutterwave.rave.models.response.ErrorResponse;

import java.util.List;

/**
 * Created by @Po10cio on 30/03/2017 for Rave.
 */

public class RaveData {
    public interface OnBankCallback {
        void onBanks(@NonNull List<Bank> banks);

        void onError(@NonNull ErrorResponse e);

        void onFailure(@NonNull Throwable t);
    }

    public interface OnChargeResponse {
        void onCharge(@NonNull ChargeResponse chargeResponse);

        void onError(@NonNull ErrorResponse e);

        void onFailure(@NonNull Throwable t);
    }

    public interface OnCardValidateCallback {
        void onCardValidate(@NonNull CardValidateResponse response);

        void onError(@NonNull ErrorResponse e);

        void onFailure(@NonNull Throwable t);
    }

    public interface OnAccountValidateCallback {
        void onAccountValidate(@NonNull AccountValidateResponse response);

        void onError(@NonNull ErrorResponse e);

        void onFailure(@NonNull Throwable t);
    }
}

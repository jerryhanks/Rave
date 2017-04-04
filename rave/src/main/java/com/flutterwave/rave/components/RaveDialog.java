package com.flutterwave.rave.components;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.flutterwave.rave.R;
import com.flutterwave.rave.models.data.Bank;
import com.flutterwave.rave.models.data.RaveData;
import com.flutterwave.rave.models.request.AccountChargeRequestData;
import com.flutterwave.rave.models.request.BaseRequestData;
import com.flutterwave.rave.models.request.CardChargeRequestData;
import com.flutterwave.rave.models.request.ShortCodeRequestData;
import com.flutterwave.rave.models.response.AccountValidateRequest;
import com.flutterwave.rave.models.response.AccountValidateResponse;
import com.flutterwave.rave.models.response.CardValidateRequest;
import com.flutterwave.rave.models.response.CardValidateResponse;
import com.flutterwave.rave.models.response.ChargeRequest;
import com.flutterwave.rave.models.response.ChargeResponse;
import com.flutterwave.rave.models.response.ErrorResponse;
import com.flutterwave.rave.models.response.RaveResponse;
import com.flutterwave.rave.models.response.ResponseData;
import com.flutterwave.rave.service.RaveClient;
import com.flutterwave.rave.utils.RaveAuthModel;
import com.flutterwave.rave.utils.RaveUtil;
import com.google.common.base.Optional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Shittu on 17/12/2016.
 */

public class RaveDialog extends Dialog implements View.OnKeyListener, View.OnClickListener {

    private static final String PAY_FORMAT = "PAY NGN %.2f";
    private static final String PRICE_FORMAT = "NGN %.2f";


    private static final String VBV_SECURE_CODE = "VBV_SECURE_CODE";
    private static final String NO_AUTH = "NO_AUTH";
    private static final String PIN = "PIN";
    private static final String RANDOM_DEBIT = "RANDOM_DEBIT";


    private static final int CARD_DETAILS = 1;
    private static final int ACCOUNT_DETAILS = 2;
    private static final int ALERT_MESSAGE = 3;
    private static final int CARD_AND_ALERT_MESSAGE = 4;
    private static final int OTP_VIEW = 5;
    private static final int OTP_AND_ALERT_MESSAGE = 6;
    private static final int ACCOUNT_AND_ALERT_MESSAGE = 7;
    private static final String TAG = RaveDialog.class.getCanonicalName();
    private final OnRaveResponseCallback mListener;

    private Button mPayBtn;
    private RaveData mRaveData;

    private RadioButton mCardSelectButton;
    private RadioButton mAccountSelectButton;

    private LinearLayout mCardDetailView;
    private LinearLayout mAccountDetailView;
    private LinearLayout mAlertMessageView;
    private LinearLayout mOtpDetailView;

    private EditText mCardNumber;
    private EditText mInputAmountCard;
    private EditText mInputAmountAccount;
    private EditText mUserToken;
    private EditText mExpiryDate;
    private EditText mCvv;
    private EditText mAccountNumber;
    private EditText mOtpNumber;
    private EditText mCardPin;
    private EditText mAmountCharge;
    private TextView mAlertMessage;

    private Spinner mBankSpinner;
    private Spinner mOtpSpinner;

    private WebView mWebView;
    private ProgressDialog mProgressBar;
    private CheckBox mRememberBox;
    private CheckBox mUseToken;

    //TODO: replace with enums
    private boolean mIsCardTransaction = true;
    private boolean mIsAccountValidate = true;
    private boolean mShouldRememberCardDetails = false;
    private boolean mShouldUseToken = false;

    private String mAuthUrlString = "";
    private String mValidateTxRef = "";
    private String mUserCode = "";
    private List<String> mBankNames = new ArrayList<String>();
    private List<String> mBankCodes = new ArrayList<String>();
    private TextView itemPrice;


    public RaveDialog(@NonNull Context context, @NonNull RaveData data) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_layout);

        mRaveData = data;
        fetchBanks();

        setWidgets();
        mListener = null;

    }

    public RaveDialog(@NonNull Context context, @NonNull RaveData data, @NonNull OnRaveResponseCallback responseCallback) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_layout);

        mRaveData = data;
        fetchBanks();

        setWidgets();

        mListener = responseCallback;
    }

    private void setWidgets() {

        //link and set pay button action
        mPayBtn = (Button) findViewById(R.id.pay_btn);
        mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
        mPayBtn.setOnClickListener(this);

        //set item image
        ImageView itemImageView = (ImageView) findViewById(R.id.item_img);
        Bitmap bitmap = Optional.fromNullable(mRaveData.getItemImage()).or(
                BitmapFactory.decodeResource(getContext().getResources(), R.drawable.coke)
        );

        RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
        roundDrawable.setCircular(true);
        itemImageView.setImageDrawable(roundDrawable);


        //set item name
        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setText(mRaveData.getItemName());

        //set item description
        TextView itemDescription = (TextView) findViewById(R.id.item_description);
        itemDescription.setText(mRaveData.getItemDescription());

        //set item price
        itemPrice = (TextView) findViewById(R.id.item_price);
        itemPrice.setText(String.format(Locale.getDefault(), PRICE_FORMAT, mRaveData.getItemPrice()));

        //set message text view
        mAlertMessage = (TextView) findViewById(R.id.message_text_view);

        //set alert message view
        mAlertMessageView = (LinearLayout) findViewById(R.id.alert_message_view);

        //set close image button
        ImageButton mCloseButton = (ImageButton) findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(this);

        //set Radio Buttons
        mCardSelectButton = (RadioButton) findViewById(R.id.card_segment_btn);
        mAccountSelectButton = (RadioButton) findViewById(R.id.account_segment_btn);

        //set detail view
        mCardDetailView = (LinearLayout) findViewById(R.id.card_segment_view);
        mAccountDetailView = (LinearLayout) findViewById(R.id.account_segment_view);
        mOtpDetailView = (LinearLayout) findViewById(R.id.otp_view);

        mOtpNumber = (EditText) findViewById(R.id.otp_number);

        //set onClick Listener
        mCardSelectButton.setOnClickListener(this);
        mAccountSelectButton.setOnClickListener(this);

        //set cvv text field
        mCvv = (EditText) findViewById(R.id.cvv);

        //set account number text field
        mAccountNumber = (EditText) findViewById(R.id.account_number);

        //set the input text field
        mInputAmountCard = (EditText) findViewById(R.id.amount_card_segment);

        mInputAmountAccount = (EditText) findViewById(R.id.amount_account_segment);

        //check if mRaveData amount is set
        if (mRaveData.getItemPrice() == 0) {
            //item price is set to zero so show the input
            mInputAmountCard.setVisibility(View.VISIBLE);
            //add text watcher
            mInputAmountCard.addTextChangedListener(new InputAmountWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    //update the payButton and the item price tv
                    updateItemPriceTV(charSequence, count);
                }
            });

            mInputAmountAccount.setVisibility(View.VISIBLE);
            mInputAmountAccount.addTextChangedListener(new InputAmountWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                    //update the payButton and the item price tv
                    updateItemPriceTV(charSequence, count);
                }
            });

        }


        //set expiry date text field
        mExpiryDate = (EditText) findViewById(R.id.expiry_date);
        mExpiryDate.setOnKeyListener(this);

        //set Card number text field
        mCardNumber = (EditText) findViewById(R.id.card_number);
        mCardNumber.setOnKeyListener(this);

        mWebView = (WebView) findViewById(R.id.web_view);
        mRememberBox = (CheckBox) findViewById(R.id.remember_card);
        mUseToken = (CheckBox) findViewById(R.id.use_token);
        mUserToken = (EditText) findViewById(R.id.user_token);
        mCardPin = (EditText) findViewById(R.id.card_pin);
        mAmountCharge = (EditText) findViewById(R.id.amount_charged);

        if (mRaveData.getAuthModel().equals(RaveAuthModel.PIN)) {
            mCardPin.setVisibility(View.VISIBLE);
            //If the pin Ed is visible, make it to have imeOptions of done
            //and the otp to have imeOptions next
            mCardPin.setImeOptions(EditorInfo.IME_ACTION_DONE);
            mCvv.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            mCvv.setImeOptions(EditorInfo.IME_ACTION_DONE);
            mCardPin.setVisibility(View.GONE);
        }


        mRememberBox.setOnClickListener(this);

        mUseToken.setOnClickListener(this);

    }

    private void updateItemPriceTV(CharSequence charSequence, int count) {
        double amount = charSequence.length() == 0 ? 0.0 : Double.parseDouble(charSequence.toString());
        if (count >= 0) {
            itemPrice.setText(String.format(Locale.getDefault(), PRICE_FORMAT, amount));
            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, amount));
            mRaveData.setmItemPrice(amount);
        }
    }

    private void fetchBanks() {
        RaveClient.getBanks(new com.flutterwave.rave.data.RaveData.OnBankCallback() {
            @Override
            public void onBanks(List<Bank> banks) {
                // can't use java 8 stream cos of api level restriction
                for (Bank bank : banks) {
                    mBankNames.add(bank.getName());
                    mBankCodes.add(bank.getCode());
                }

                //set spinners
                mBankSpinner = (Spinner) findViewById(R.id.bank_spinner);
                mOtpSpinner = (Spinner) findViewById(R.id.otp_spinner);

                // Create an ArrayAdapter using the string array and a default spinner
                ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, mBankNames);

                ArrayAdapter<CharSequence> otpAdapter = ArrayAdapter
                        .createFromResource(getContext(), R.array.otp_option_array,
                                android.R.layout.simple_spinner_item);

                // Specify the layout to use when the list of choices appears
                bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                otpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                mBankSpinner.setAdapter(bankAdapter);
                mOtpSpinner.setAdapter(otpAdapter);
            }

            @Override
            public void onError(@NonNull ErrorResponse e) {
                Log.d(TAG, "An error occured: " + e.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error Loading Banks: " + t.getMessage());

            }
        });
    }

    private void updateCardTextViewIcon(int drawableId) {
        mCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), drawableId), null);
        mCardNumber.invalidate();
    }

    private void updateSegmentIcons(int cardDrawableId, int accountDrawableId) {
        mCardSelectButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), cardDrawableId), null, null, null);
        mAccountSelectButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), accountDrawableId), null, null, null);
        mCardSelectButton.invalidate();
        mAccountSelectButton.invalidate();
    }

    private void updateBackground(RadioButton checked, RadioButton unChecked) {
        unChecked.setBackgroundColor(Color.TRANSPARENT);
        unChecked.setTextColor(Color.BLACK);

        checked.setTextColor(Color.WHITE);
        checked.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.martinique));
    }

    // construct the right request data
    private BaseRequestData getRequestData() {
        try {
            if (mIsCardTransaction) {
                BaseRequestData data;
                String[] dateData = mExpiryDate.getText().toString().split("/");
                if (!mShouldUseToken) {
                    data = new CardChargeRequestData(
                            mRaveData.getPbfPubKey(),
                            mRaveData.getItemPrice().toString(),
                            mRaveData.getCustomerEmailAddress(),
                            mRaveData.getIp(),
                            mRaveData.getTxRef(),
                            mRaveData.getCountry(),
                            mRaveData.getCurrency(),
                            mRaveData.getFirstName(),
                            mRaveData.getLastName(),
                            mRaveData.getNarration(),
                            mRaveData.getMeta(),
                            RaveUtil.cleanText(mCardNumber.getText().toString(), " "),
                            mCvv.getText().toString(),
                            dateData[1], // month
                            dateData[0], // year
                            mRaveData.getAuthModel().equals(RaveAuthModel.PIN) ?
                                    mCardPin.getText().toString() : ""
                    );

                } else {
                    data = new ShortCodeRequestData(
                            mRaveData.getPbfPubKey(),
                            mRaveData.getItemPrice().toString(),
                            mRaveData.getCustomerEmailAddress(),
                            mRaveData.getIp(),
                            mRaveData.getTxRef(),
                            mRaveData.getCountry(),
                            mRaveData.getCurrency(),
                            mRaveData.getFirstName(),
                            mRaveData.getLastName(),
                            mRaveData.getNarration(),
                            mRaveData.getMeta(),
                            mCvv.getText().toString(),
                            mUserToken.getText().toString(),
                            mRaveData.getAuthModel().equals(RaveAuthModel.PIN) ?
                                    mCardPin.getText().toString() : ""
                    );
                }
                return data;
            } else {
                int index = mBankSpinner.getSelectedItemPosition();
                String bankCode = mBankCodes.get(index);
                return new AccountChargeRequestData(
                        mRaveData.getPbfPubKey(),
                        mRaveData.getItemPrice().toString(),
                        mRaveData.getCustomerEmailAddress(),
                        mRaveData.getIp(),
                        mRaveData.getTxRef(),
                        mRaveData.getCountry(),
                        mRaveData.getCurrency(),
                        mRaveData.getFirstName(),
                        mRaveData.getLastName(),
                        mRaveData.getNarration(),
                        mRaveData.getMeta(),
                        mAccountNumber.getText().toString(),
                        bankCode,
                        mOtpSpinner.getSelectedItem().toString()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // basic input validation
    private boolean validateInputFields() {
        boolean isValid = true;
        if (mCardDetailView.isShown()) {

            if (mInputAmountCard.getVisibility() == View.VISIBLE && mInputAmountCard.getText().length() == 0) {
                mInputAmountCard.setError(getContext().getString(R.string.enter_a_valid_amount));
            }

            if (!mShouldUseToken) {
                if (mCardNumber.getText().length() != 19) {
                    mCardNumber.setError(getContext().getString(R.string.card_field_error));
                    isValid = false;
                }

                if (mExpiryDate.getText().length() != 5) {
                    mExpiryDate.setError(getContext().getString(R.string.date_field_error));
                    isValid = false;
                }
            } else {
                if (mUserToken.getText().length() != 5) {
                    mUserToken.setError(getContext().getString(R.string.user_token_field_error));
                    isValid = false;
                }
            }
            if (mCvv.getText().length() != 3) {
                mCvv.setError(getContext().getString(R.string.cvv_field_error));
                isValid = false;
            }

            if (mRaveData.getAuthModel().equals(RaveAuthModel.PIN)) {
                if (mCardPin.getText().length() != 4) {
                    mCardPin.setError(getContext().getString(R.string.pin_field_error));
                    isValid = false;
                }
            }
        }

        if (mAccountDetailView.isShown()) {
            if (mAccountNumber.getText().length() != 10) {
                mAccountNumber.setError(getContext().getString(R.string.account_number_field_error));
                isValid = false;
            }
            if (mInputAmountAccount.getVisibility() == View.VISIBLE && mInputAmountAccount.getText().length() == 0) {
                mInputAmountAccount.setError(getContext().getString(R.string.enter_a_valid_amount));
            }

            if (mBankSpinner.getSelectedItem().toString().equals(getContext().getString(R.string.select_bank))) {
                showSpinnerError(mBankSpinner);
                isValid = false;
            }

            if (mOtpSpinner.getSelectedItem().toString().equals(getContext().getString(R.string.select_otp))) {
                showSpinnerError(mOtpSpinner);
                isValid = false;
            }
        }

        if (mOtpDetailView.isShown()) {
            if (mOtpNumber.getText().length() != 5) {
                mOtpNumber.setError(getContext().getString(R.string.otp_field_error));
                isValid = false;
            }
        }

        return isValid;
    }

    private void showSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError(getContext().getString(R.string.spinner_error));
        }
    }


//    @Deprecated
//    private void handleCardValidateResponse(Response response) {
//
//        if (response != null) {
//            Reader responseReader = response.body().charStream();
//
//            Map<String, Object> mapResponse = RaveUtil.getMapFromJsonReader(responseReader);
//            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
//            Map<String, Object> innerData = (Map<String, Object>) data.get("data");
//
//            if (response.isSuccessful() && (innerData.get("responsecode").equals("02")
//                    || innerData.get("responsecode").equals("00"))) {
//
//                // finished random debit
//
//                String msg = (String) innerData.get("responsemessage");
//                if (mShouldRememberCardDetails) {
//                    msg = (msg + "\n To avoid entering card detail on subsequent transactions," +
//                            " use the token below. \n user token : "
//                            + mUserCode);
//                }
//
//                mAlertMessage.setText(msg);
//                mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
//
//                if (mListener != null) {
//                    mListener.onResponse((Map<String, Object>) data.get("tx"));
//                }
//
//                showView(ALERT_MESSAGE);
//                mPayBtn.setText(R.string.close_form);
//                mPayBtn.setBackgroundResource(R.drawable.curved_shape);
//                mPayBtn.setTextColor(Color.BLACK);
//
//            } else {
//                //the error response does not have data
//                mAlertMessage.setText((String) mapResponse.get("message"));
//                mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                lockOrUnlockInputFields(true);
//                showView(CARD_AND_ALERT_MESSAGE);
//            }
//        } else {
//            mAlertMessage.setText(R.string.network_error);
//            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//            lockOrUnlockInputFields(true);
//            showView(CARD_AND_ALERT_MESSAGE);
//        }
//    }
//
//    @Deprecated
//    private void handleCardChargeResponse(Response response) {
//        if (response != null) {
//            Reader responseReader = response.body().charStream();
//            Map<String, Object> mapResponse = RaveUtil.getMapFromJsonReader(responseReader);
//            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
//
//            String authMode = (String) data.get("authModelUsed");
//            System.out.println("card charge response : " + data);
//            if (response.isSuccessful() && authMode != null) {
//                Map<Object, Object> chargeToken = (Map<Object, Object>) data.get("chargeToken");
//                mUserCode = (String) chargeToken.get("user_token");
//
//                Number amount = (Number) data.get("amount");
//                Number chargedAmount = (Number) data.get("charged_amount");
//                Number appFee = (Number) data.get("appfee");
//
//                // this takes care of situation where merchant bears fees charged
//                // by ensuring the app fee displayed to customer is 0
//                // since it has been paid for by merchant
//                if (amount.equals(chargedAmount)) {
//                    appFee = 0;
//                }
//
//                String amountMsg = String.format("%s + %s = %s", amount, appFee, chargedAmount);
//
//                mAmountCharge.setText(amountMsg);
//
//                switch (authMode) {
//                    case VBV_SECURE_CODE:
//                        if ((data.get("chargeResponseCode").equals("02")
//                                || data.get("chargeResponseCode").equals("00"))) {
//                            mAuthUrlString = (String) data.get("authurl");
//                            System.out.println("card charge response : " + data);
//
//                            mPayBtn.setBackgroundResource(R.drawable.curved_shape_martinique);
//                            showView(CARD_DETAILS);
//                            mPayBtn.setText(R.string.click_here);
//
//                        } else {
//                            mAlertMessage.setText((String) data.get("message"));
//                            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                            lockOrUnlockInputFields(true);
//                            showView(CARD_AND_ALERT_MESSAGE);
//                        }
//                        break;
//                    case NO_AUTH:
//                        if (data.get("chargeResponseCode").equals("00")) {
//                            System.out.println("card charge response : " + data);
//                            String msg = (String) data.get("vbvrespmessage");
//                            if (mShouldRememberCardDetails) {
//                                msg = (msg + "\n To avoid entering card detail on subsequent transactions," +
//                                        " use the token below. \n user token : "
//                                        + mUserCode);
//                            }
//
//                            mAlertMessage.setText(msg);
//                            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
//
//                            if (mListener != null) {
//                                mListener.onResponse((Map<String, Object>) data.get("tx"));
//                            }
//
//                            showView(ALERT_MESSAGE);
//                            mPayBtn.setText(R.string.close_form);
//                            mPayBtn.setBackgroundResource(R.drawable.curved_shape);
//                            mPayBtn.setTextColor(Color.BLACK);
//
//                        } else {
//                            mAlertMessage.setText((String) data.get("message"));
//                            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                            lockOrUnlockInputFields(true);
//                            showView(CARD_AND_ALERT_MESSAGE);
//                        }
//                        break;
//                    case RANDOM_DEBIT:
//                        if ((data.get("chargeResponseCode").equals("02")
//                                || data.get("chargeResponseCode").equals("00"))) {
//                            //no auth url returned
//                            mOtpNumber.setEnabled(true);
//                            mValidateTxRef = (String) data.get("flwRef");
//                            mPayBtn.setText(R.string.validate_otp);
//                            mIsAccountValidate = false;
//                            showView(OTP_VIEW);
//                        } else {
//                            mAlertMessage.setText((String) data.get("message"));
//                            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                            lockOrUnlockInputFields(true);
//                            showView(CARD_AND_ALERT_MESSAGE);
//                        }
//                        break;
//                    case PIN:
//                        if ((data.get("chargeResponseCode").equals("02")
//                                || data.get("chargeResponseCode").equals("00"))) {
//                            //no auth url returned
//                            mOtpNumber.setEnabled(true);
//                            mValidateTxRef = (String) data.get("flwRef");
//                            mPayBtn.setText(R.string.validate_otp);
//                            mIsAccountValidate = false;
//                            showView(OTP_VIEW);
//                        } else {
//                            mAlertMessage.setText((String) data.get("message"));
//                            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                            lockOrUnlockInputFields(true);
//                            showView(CARD_AND_ALERT_MESSAGE);
//                        }
//                        break;
//                    default:
//                        // invalid auth model used
//                        System.out.println("Invalid auth model used : " + (String) data.get("authModelUsed"));
//                        mAlertMessage.setText(("Invalid auth model used : " + (String) data.get("authModelUsed")));
//                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                        showView(ALERT_MESSAGE);
//                        mPayBtn.setText(R.string.close_form);
//
//                        break;
//
//                }
//            } else {
//                mAlertMessage.setText((String) data.get("message"));
//                mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//                lockOrUnlockInputFields(true);
//                showView(CARD_AND_ALERT_MESSAGE);
//            }
//
//        } else {
//            mAlertMessage.setText(R.string.network_error);
//            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//            lockOrUnlockInputFields(true);
//            showView(CARD_AND_ALERT_MESSAGE);
//        }
//    }
//
//    @Deprecated
//    private void handleGetBankResponse(Response response) {
//
//        if (response != null) {
//            if (response.isSuccessful()) {
//                try {
//                    String responseString = response.body().string();
//                    Map<String, Object> mapResponse = RaveUtil.getMapFromJsonString(responseString);
//                    List<Map<String, Object>> bankObjects = (List<Map<String, Object>>) mapResponse.get("data");
//
//                    // can't use java 8 stream cos of api level restriction
//                    for (Map<String, Object> bankObject : bankObjects) {
//                        mBankNames.add((String) bankObject.get("name"));
//                        mBankCodes.add((String) bankObject.get("code"));
//                    }
//
//                    //set spinners
//                    mBankSpinner = (Spinner) findViewById(R.id.bank_spinner);
//                    mOtpSpinner = (Spinner) findViewById(R.id.otp_spinner);
//
//                    // Create an ArrayAdapter using the string array and a default spinner
//                    ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(getContext(),
//                            android.R.layout.simple_spinner_item, mBankNames);
//
//                    ArrayAdapter<CharSequence> otpAdapter = ArrayAdapter
//                            .createFromResource(getContext(), R.array.otp_option_array,
//                                    android.R.layout.simple_spinner_item);
//
//                    // Specify the layout to use when the list of choices appears
//                    bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    otpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                    // Apply the adapter to the spinner
//                    mBankSpinner.setAdapter(bankAdapter);
//                    mOtpSpinner.setAdapter(otpAdapter);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e(TAG, "Failed to fetch list of bank objects, with response code : " + response.code());
//            }
//        } else {
//            Log.e(TAG, "Could not fetch list of bank objects, response object is : " + response);
//        }
//    }
//
//    @Deprecated
//    private void handleAccountChargeResponse(Response response) {
//        if (response != null) {
//            try {
//                String responseString = response.body().string();
//                Map<String, Object> mapResponse = RaveUtil.getMapFromJsonString(responseString);
//                Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
//
//                System.out.println("account charge response : " + data);
//                if (response.isSuccessful() && (data.get("chargeResponseCode").equals("02") || data.get("chargeResponseCode").equals("00"))) {
//
//                    Number amount = (Number) data.get("amount");
//                    Number chargedAmount = (Number) data.get("charged_amount");
//                    Number appFee = (Number) data.get("appfee");
//
//                    // this takes care of situation where merchant bears fees charged
//                    // by ensuring the app fee displayed to customer is 0
//                    // since it has been paid for by merchant
//                    if (amount.equals(chargedAmount)) {
//                        appFee = 0;
//                    }
//
//                    String amountMsg = String.format("%s + %s = %s", amount, appFee, chargedAmount);
//
//                    mAmountCharge.setText(amountMsg);
//
//                    mOtpNumber.setEnabled(true);
//                    mValidateTxRef = (String) data.get("flwRef"); // TODO: 22/03/2017 Verify why this has to be txRef and not flwRef
//                    mPayBtn.setText(R.string.validate_otp);
//                    mIsAccountValidate = true;
//                    showView(OTP_VIEW);
//                } else {
//                    // show error alert message  and account view
//                    lockOrUnlockInputFields(true);
//                    if (data.get("code").equals("LIMIT_ERR") && mInputAmountAccount.getVisibility() == View.VISIBLE) {
//                        mInputAmountAccount.setError((String) data.get("message"));
//                    } else {
//                        mAlertMessage.setText((String) data.get("message"));
//                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                        showView(ACCOUNT_AND_ALERT_MESSAGE);
//                    }
//                    mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // response is null
//            mAlertMessage.setText(R.string.network_error);
//            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//            mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
//            showView(ACCOUNT_AND_ALERT_MESSAGE);
//        }
//    }
//
//    @Deprecated
//    private void handleAccountValidateResponse(Response response) {
//        if (response != null) {
//            try {
//                String responseString = response.body().string();
//                Map<String, Object> mapResponse = RaveUtil.getMapFromJsonString(responseString);
//                Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
//
//                System.out.print("Account Validate Response StatusData: " + response);
//
//                if (response.isSuccessful()) {
//                    if (data.get("acctvalrespcode").equals("02") || data.get("acctvalrespcode").equals("00")) {
//
//                        if (mListener != null) {
//                            mListener.onResponse(data);
//                        }
//
//                        mAlertMessage.setText((String) data.get("acctvalrespmsg"));
//                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
//                        showView(ALERT_MESSAGE);
//
//                        mPayBtn.setText(R.string.close_form);
//                        mPayBtn.setBackgroundResource(R.drawable.curved_shape);
//                        mPayBtn.setTextColor(Color.BLACK);
//                    } else {
//                        // show error alert message
//                        mAlertMessage.setText((String) data.get("acctvalrespmsg"));
//                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                        mPayBtn.setText(R.string.close_form);
//                        mPayBtn.setBackgroundResource(R.drawable.curved_shape);
//                        mPayBtn.setTextColor(Color.BLACK);
//                        lockOrUnlockInputFields(true);
//                        showView(ALERT_MESSAGE);
//                    }
//                } else {
//                    // show error alert message  and otp view
//                    //the error response does not have data
//                    mAlertMessage.setText((String) mapResponse.get("message"));
//                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//                    mPayBtn.setText(R.string.close_form);
//                    mPayBtn.setBackgroundResource(R.drawable.curved_shape);
//                    mPayBtn.setTextColor(Color.BLACK);
//                    lockOrUnlockInputFields(true);
//                    showView(ALERT_MESSAGE);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            //response is null
//            mAlertMessage.setText(R.string.network_error);
//            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
//            mPayBtn.setText(R.string.validate_otp);
//            lockOrUnlockInputFields(true);
//            showView(OTP_AND_ALERT_MESSAGE);
//        }
//    }

    private void showView(int viewNumber) {
        BigDecimal amount = BigDecimal.valueOf(mRaveData.getItemPrice());
        switch (viewNumber) {
            case CARD_DETAILS:
                mInputAmountCard.setText(amount.toPlainString());
                mCardDetailView.setVisibility(View.VISIBLE);
                mAccountDetailView.setVisibility(View.GONE);
                mAlertMessageView.setVisibility(View.GONE);
                mOtpDetailView.setVisibility(View.GONE);
                break;
            case ACCOUNT_DETAILS:
                mInputAmountAccount.setText(amount.toPlainString());
                mCardDetailView.setVisibility(View.GONE);
                mAccountDetailView.setVisibility(View.VISIBLE);
                mAlertMessageView.setVisibility(View.GONE);
                mOtpDetailView.setVisibility(View.GONE);
                break;
            case ALERT_MESSAGE:
                mCardDetailView.setVisibility(View.GONE);
                mAccountDetailView.setVisibility(View.GONE);
                mAlertMessageView.setVisibility(View.VISIBLE);
                mOtpDetailView.setVisibility(View.GONE);
                break;
            case CARD_AND_ALERT_MESSAGE:
                mCardDetailView.setVisibility(View.VISIBLE);
                mAccountDetailView.setVisibility(View.GONE);
                mAlertMessageView.setVisibility(View.VISIBLE);
                mOtpDetailView.setVisibility(View.GONE);
                break;
            case ACCOUNT_AND_ALERT_MESSAGE:
                mCardDetailView.setVisibility(View.GONE);
                mAccountDetailView.setVisibility(View.VISIBLE);
                mAlertMessageView.setVisibility(View.VISIBLE);
                mOtpDetailView.setVisibility(View.GONE);
                break;
            case OTP_AND_ALERT_MESSAGE:
                mCardDetailView.setVisibility(View.GONE);
                mAccountDetailView.setVisibility(View.GONE);
                mAlertMessageView.setVisibility(View.VISIBLE);
                mOtpDetailView.setVisibility(View.VISIBLE);
                break;
            case OTP_VIEW:
                mCardDetailView.setVisibility(View.GONE);
                mAccountDetailView.setVisibility(View.GONE);
                mAlertMessageView.setVisibility(View.GONE);
                mOtpDetailView.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }


    private void lockOrUnlockInputFields(boolean unLock) {
        mCardSelectButton.setEnabled(unLock);
        mAccountSelectButton.setEnabled(unLock);
        mAccountNumber.setEnabled(unLock);
        if (mBankSpinner != null) { // might be null if there's no internet connect on client device
            mBankSpinner.setEnabled(unLock);
        }
        if (mOtpSpinner != null) {
            mOtpSpinner.setEnabled(unLock);
        }

        //radio buttons
        mCardSelectButton.setEnabled(unLock);
        mAccountSelectButton.setEnabled(unLock);

        mInputAmountAccount.setEnabled(unLock);
        mInputAmountCard.setEnabled(unLock);
        mCardNumber.setEnabled(unLock);
        mCvv.setEnabled(unLock);
        mExpiryDate.setEnabled(unLock);
        mOtpNumber.setEnabled(unLock);
        mCardPin.setEnabled(unLock);
        mUseToken.setEnabled(unLock);


        if (mUserToken != null) {
            mUserToken.setEnabled(unLock); // Throws null pointer exception
        }
        mRememberBox.setEnabled(unLock);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        int id = view.getId();
        if (id == R.id.card_number) {
            if (keyCode != 67) {
                String cardText = mCardNumber.getText().toString();
                String validText = RaveUtil.cleanText(cardText, " ");

                int visaNumber = 0;
                int masterNumber = 0;

                if (validText.length() > 0) {
                    visaNumber = Integer.parseInt(validText.substring(0, 1));
                }

                if (validText.length() > 1) {
                    masterNumber = Integer.parseInt(validText.substring(0, 2));
                }

                if (visaNumber == 4) {
                    updateCardTextViewIcon(R.drawable.visa);
                } else if (masterNumber >= 51 && masterNumber <= 55) {
                    updateCardTextViewIcon(R.drawable.mastercard);
                } else {
                    updateCardTextViewIcon(R.drawable.bank_card_filled);
                }

                // add space after every 4 characters
                if (validText.length() % 4 == 0) {
                    // to put cursor in right position
                    String text = RaveUtil.addPadding(" ", validText, 4);
                    mCardNumber.setText("");
                    mCardNumber.append(text);
                }
            }
        } else if (id == R.id.expiry_date) {
            String cleanText = mExpiryDate.getText().toString();
            if (keyCode != 67 && cleanText.length() == 2) {
                // add `/` after first 2 characters
                // to put cursor in right position
                String text = RaveUtil.addPadding("/", cleanText, 2);
                mExpiryDate.setText("");
                mExpiryDate.append(text);
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.card_segment_btn) {
            updateBackground(mCardSelectButton, mAccountSelectButton);
            showView(CARD_DETAILS);
            mIsCardTransaction = true;
            updateSegmentIcons(R.drawable.bank_card_small, R.drawable.account_small_filled);
        } else if (id == R.id.account_segment_btn) {
            updateBackground(mAccountSelectButton, mCardSelectButton);
            showView(ACCOUNT_DETAILS);
            mIsCardTransaction = false;
            updateSegmentIcons(R.drawable.bank_card_filled_small, R.drawable.account_small);

        } else if (id == R.id.remember_card) {
            mShouldRememberCardDetails = ((CheckBox) view).isChecked();
        } else if (id == R.id.use_token) {
            mShouldUseToken = ((CheckBox) view).isChecked();

            if (mShouldUseToken) {
                mCardNumber.setVisibility(View.GONE);
                mExpiryDate.setVisibility(View.GONE);
                mRememberBox.setVisibility(View.GONE);
//                    if (mRaveData.ismIsPinAuthModel()) {
//                        mCardPin.setVisibility(View.GONE);
//                    }
                mUserToken.setVisibility(View.VISIBLE);
            } else {
                mUserToken.setVisibility(View.GONE);
                mCardNumber.setVisibility(View.VISIBLE);
                mExpiryDate.setVisibility(View.VISIBLE);
                mRememberBox.setVisibility(View.VISIBLE);
//                    if (mRaveData.ismIsPinAuthModel()) {
//                        mCardPin.setVisibility(View.VISIBLE);
//                    }
            }
        } else if (id == R.id.close_btn) {
            dismiss();
        } else if (id == R.id.pay_btn) {
            try {
                if (mPayBtn.getText().toString().equals(getContext().getString(R.string.click_here))) {
                    // complete card validation
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.addJavascriptInterface(new WebViewJavaScriptInterface(), "INTERFACE");
                    mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

                    mProgressBar = ProgressDialog.show(getContext(), "Verifying Transaction", "Authenticating...");

                    mWebView.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            Log.i(TAG, "Finished loading URL: " + url);
                            if (mProgressBar.isShowing()) {
                                mProgressBar.dismiss();
                            }
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            mWebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                                        }
                                    },
                                    3000);

                        }
                    });
                    //disable button while web view loads
                    mPayBtn.setText(R.string.please_wait);
                    mPayBtn.setEnabled(false);
                    mWebView.setVisibility(View.VISIBLE);
                    mWebView.loadUrl(mAuthUrlString);

                } else if (mPayBtn.getText().toString().equals(getContext().getString(R.string.validate_otp))) {
                    // otp validation
                    if (validateInputFields()) {
                        String otp = mOtpNumber.getText().toString();

                        if (mIsAccountValidate) {
                            showProgressView();
                            RaveClient.validateAccountCharge(new AccountValidateRequest(mRaveData.getPbfPubKey(),
                                    mValidateTxRef, otp), new com.flutterwave.rave.data.RaveData.OnAccountValidateCallback() {
                                @Override
                                public void onAccountValidate(@NonNull AccountValidateResponse response) {

                                    if (response.getStatus().equals("success")) {
                                        handleAccountValidateResponse(response);
                                    }
                                }

                                @Override
                                public void onError(@NonNull ErrorResponse e) {
                                    showValidationFailedView(e.getMessage());

                                }

                                @Override
                                public void onFailure(@NonNull Throwable t) {
                                    showNetworkErrorMessage();
                                }
                            });
                        } else {

                            showProgressView();
                            RaveClient.validateCardCharge(new CardValidateRequest(mRaveData.getPbfPubKey(),
                                    mValidateTxRef, otp), new com.flutterwave.rave.data.RaveData.OnCardValidateCallback() {
                                @Override
                                public void onCardValidate(@NonNull CardValidateResponse response) {
                                    if (response.getStatus().equals("success")) {
                                        handleCardValidateResponse(response);
                                    }

                                }

                                @Override
                                public void onError(@NonNull ErrorResponse e) {

                                    showUnSuccessfulMessage(e.getMessage());

                                }

                                @Override
                                public void onFailure(@NonNull Throwable t) {
                                    showNetworkErrorMessage();
                                }
                            });

                        }
                    }

                } else if (mPayBtn.getText().toString().equals(getContext().getString(R.string.close_form))) {
                    // close form
                    dismiss();
                } else {
                    // charge request
                    if (validateInputFields()) {
                        BaseRequestData requestData = getRequestData();
                        String jsonData = RaveUtil.getJsonStringFromRequestData(requestData);
                        String encryptedData = RaveUtil.getEncryptedData(jsonData, mRaveData.getSecretKey());

                        //set request params
                        showProgressView();
                        RaveClient.charge(new ChargeRequest(mRaveData.getPbfPubKey(), encryptedData, "3DES-24"), new com.flutterwave.rave.data.RaveData.OnChargeResponse() {
                            @Override
                            public void onCharge(@NonNull ChargeResponse chargeResponse) {
                                if (chargeResponse.getStatus().equals("success")) {
                                    updateOTPView(chargeResponse.getData());
                                } else {

                                    showUnSuccessfulMessage(chargeResponse.getMessage());
                                }

                            }

                            @Override
                            public void onError(@NonNull ErrorResponse e) {
                                //check if the amount Ed is visible and make it show the Error
                                if (e.getData().getCode().equals("LIMIT_ERR")) {
                                    if (mInputAmountCard.getVisibility() == View.VISIBLE && mIsCardTransaction) {
                                        mInputAmountCard.setError(e.getData().getMessage());
                                    } else if (mInputAmountAccount.getVisibility() == View.VISIBLE && !mIsCardTransaction) {
                                        mInputAmountAccount.setError(e.getData().getMessage());
                                    } else {
                                        showUnSuccessfulMessage(e.getMessage());
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d(TAG, "An error occurred :" + t.getMessage());
                                showNetworkErrorMessage();

                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showValidationFailedView(String message) {
        mAlertMessage.setText(message);
        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
        mPayBtn.setText(R.string.close_form);
        mPayBtn.setBackgroundResource(R.drawable.curved_shape);
        mPayBtn.setTextColor(Color.BLACK);
        lockOrUnlockInputFields(true);
        showView(ALERT_MESSAGE);

    }

    private void handleAccountValidateResponse(AccountValidateResponse response) {
        if (response.getData().getAcctvalRespCode().equals("02") || response.getData().getAcctvalRespCode().equals("00")) {

            if (mListener != null) {
                mListener.onResponse(response);
            }

            mAlertMessage.setText(response.getData().getAcctvalRespMsg());
            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
            showView(ALERT_MESSAGE);

            mPayBtn.setText(R.string.close_form);
            mPayBtn.setBackgroundResource(R.drawable.curved_shape);
            mPayBtn.setTextColor(Color.BLACK);
        } else {
            // show error alert message
            mAlertMessage.setText(response.getData().getAcctvalRespMsg());
            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
            mPayBtn.setText(R.string.close_form);
            mPayBtn.setBackgroundResource(R.drawable.curved_shape);
            mPayBtn.setTextColor(Color.BLACK);
            lockOrUnlockInputFields(true);
            showView(ALERT_MESSAGE);
        }

    }

    private void handleCardValidateResponse(CardValidateResponse response) {
        if (response.getData().getStatusData().getResponseCode().equals("02")
                || response.getData().getStatusData().getResponseCode().equals("00")) {

            // finished random debit

            String msg = response.getData().getContent().getVbvRespMessage();
            if (mShouldRememberCardDetails) {
                msg = (msg + "\n To avoid entering card detail on subsequent transactions," +
                        " use the token below. \n user token : "
                        + mUserCode);
            }


            if (mListener != null) {
                mListener.onResponse(response);
            }

            mAlertMessage.setText(msg);
            mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
            showView(ALERT_MESSAGE);
            mPayBtn.setText(R.string.close_form);
            mPayBtn.setBackgroundResource(R.drawable.curved_shape);
            mPayBtn.setTextColor(Color.BLACK);

        }


    }

    private void showUnSuccessfulMessage(String massage) {
        mAlertMessage.setText(massage);
        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
        mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
        lockOrUnlockInputFields(true);
        showView(mIsCardTransaction ? CARD_AND_ALERT_MESSAGE : ACCOUNT_AND_ALERT_MESSAGE);


    }

    private void showNetworkErrorMessage() {
        mAlertMessage.setText(R.string.network_error);
        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
        mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
        lockOrUnlockInputFields(true);
        showView(mIsCardTransaction ? CARD_AND_ALERT_MESSAGE : ACCOUNT_AND_ALERT_MESSAGE);
    }

    private void updateOTPView(ResponseData data) {
        if (mIsCardTransaction) {
            handleCardChargeResponse(data);
        } else {
            handleAccountChargeResponse(data);
        }
    }

    private void handleAccountChargeResponse(ResponseData data) {
        if (data.getChargeResponseCode().equals("02") || data.getChargeResponseCode().equals("00")) {

            double amount = data.getAmount();
            double appFee = data.getAppFee();
            double chargedAmount = data.getChargedAmount();
            if (amount == chargedAmount) {
                appFee = 0;
            }

            String amountMsg = String.format("%s + %s = %s", amount, appFee, chargedAmount);

            mAmountCharge.setText(amountMsg);

            mOtpNumber.setEnabled(true);
            mValidateTxRef = data.getFlwRef();
            mPayBtn.setText(R.string.validate_otp);
            mIsAccountValidate = true;
            showView(OTP_VIEW);
        }

    }

    private void handleCardChargeResponse(ResponseData data) {
        String amountMsg = String.format("%s + %s = %s", data.getAmount(), data.getAppFee(), data.getChargedAmount());
        mAmountCharge.setText(amountMsg);
        Log.d(TAG, "StatusData: " + data.toString());
        switch (data.getAuthModelUsed()) {
            case VBV_SECURE_CODE:
                if ((data.getChargeResponseCode().equals("02")
                        || data.getChargeResponseCode().equals("00"))) {
                    mAuthUrlString = data.getAuthUrl();
                    System.out.println("card charge response : " + data);

                    mPayBtn.setBackgroundResource(R.drawable.curved_shape_martinique);
                    showView(CARD_DETAILS);
                    mPayBtn.setText(R.string.click_here);

                } else {
                    mAlertMessage.setText(data.getVbvRespMessage()); // TODO: 30/03/2017 This may not work
                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                    mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
                    lockOrUnlockInputFields(true);
                    showView(CARD_AND_ALERT_MESSAGE);
                }
                break;
            case NO_AUTH:
                if (data.getChargeResponseCode().equals("00")) {
                    System.out.println("card charge response : " + data);
                    String msg = data.getVbvRespMessage();
                    if (mShouldRememberCardDetails) {
                        msg = (msg + "\n To avoid entering card detail on subsequent transactions," +
                                " use the token below. \n user token : "
                                + mUserCode);
                    }

                    mAlertMessage.setText(msg);
                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);

                    // TODO: 30/03/2017 Bring back Listeners
//                    if (mListener != null) {
//                        mListener.onResponse((Map<String, Object>) data.get("tx"));
//                    }

                    showView(ALERT_MESSAGE);
                    mPayBtn.setText(R.string.close_form);
                    mPayBtn.setBackgroundResource(R.drawable.curved_shape);
                    mPayBtn.setTextColor(Color.BLACK);

                } else {
                    mAlertMessage.setText(data.getVbvRespMessage());// TODO: 30/03/2017 This may Not work
                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                    mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
                    lockOrUnlockInputFields(true);
                    showView(CARD_AND_ALERT_MESSAGE);
                }
                break;
            case RANDOM_DEBIT:
                if ((data.getChargeResponseCode().equals("02")
                        || data.getChargeResponseCode().equals("00"))) {
                    //no auth url returned
                    mOtpNumber.setEnabled(true);
                    mValidateTxRef = data.getFlwRef();
                    mPayBtn.setText(R.string.validate_otp);
                    mIsAccountValidate = false;
                    showView(OTP_VIEW);
                } else {
                    mAlertMessage.setText(data.getVbvRespMessage()); // TODO: 30/03/2017 This may Not work
                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                    mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
                    lockOrUnlockInputFields(true);
                    showView(CARD_AND_ALERT_MESSAGE);
                }
                break;
            case PIN:
                if ((data.getChargeResponseCode().equals("02")
                        || data.getChargeResponseCode().equals("00"))) {
                    //no auth url returned
                    mOtpNumber.setEnabled(true);
                    mValidateTxRef = data.getFlwRef();
                    mPayBtn.setText(R.string.validate_otp);
                    mIsAccountValidate = false;
                    showView(OTP_VIEW);
                } else {
                    mAlertMessage.setText(data.getVbvRespMessage());
                    mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                    mPayBtn.setText(String.format(Locale.getDefault(), PAY_FORMAT, mRaveData.getItemPrice()));
                    lockOrUnlockInputFields(true);
                    showView(CARD_AND_ALERT_MESSAGE);
                }
                break;
            default:
                // invalid auth model used
                Log.d(TAG, "Invalid auth model used : " + data.getAuthModelUsed());
                mAlertMessage.setText("Invalid auth model used : " + data.getAuthModelUsed());
                mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                showView(ALERT_MESSAGE);
                mPayBtn.setText(R.string.close_form);

                break;

        }
    }

    /* An instance of this class will be registered as a JavaScript interface */
    class WebViewJavaScriptInterface {
        public WebViewJavaScriptInterface() {
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;

            mAlertMessage.post(new Runnable() {
                public void run() {
                    Map<String, Object> mapResponse = RaveUtil.getMapFromJsonString(content);

                    System.out.println("response in web view : " + mapResponse);
                    mWebView.setVisibility(View.GONE);
                    String errorCode = (String) mapResponse.get("code");
                    if (errorCode != null && errorCode.equals("SERV_ERR")) {
                        mAlertMessage.setText((String) mapResponse.get("message"));
                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_dark_pastel_red);
                    } else {
                        String msg = (String) mapResponse.get("vbvrespmessage");
                        Map<Object, Object> chargeToken = (Map<Object, Object>) mapResponse.get("chargeToken");
                        if (mShouldRememberCardDetails) {
                            msg = (msg + "\n To avoid entering card detail on subsequent transactions," +
                                    " use the token below. \n user token : "
                                    + chargeToken.get("user_token"));
                        }
                        mAlertMessage.setText(msg);
                        mAlertMessage.setBackgroundResource(R.drawable.curved_shape_curious_blue);
                    }
                    showView(ALERT_MESSAGE);
                    mPayBtn.setEnabled(true);
                    mPayBtn.setText(R.string.close_form);
                    mPayBtn.setBackgroundResource(R.drawable.curved_shape);
                    mPayBtn.setTextColor(Color.BLACK);
                }
            });
        }
    }


    /**
     * Interface to return response so that the user details can be updatede
     */

    public interface OnRaveResponseCallback {
        void onResponse(RaveResponse response);
    }

    private void showProgressView() {
        mPayBtn.setText(R.string.please_wait);
    }
}

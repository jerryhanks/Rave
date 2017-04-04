package com.flutterwave.raveApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.flutterwave.rave.components.RaveDialog;
import com.flutterwave.rave.models.data.RaveData;
import com.flutterwave.rave.models.response.RaveResponse;
import com.flutterwave.rave.utils.RaveAuthModel;
import com.flutterwave.rave.utils.RaveUtil;

import flutterwave.com.raveApp.R;

public class MainActivity extends AppCompatActivity implements RaveDialog.OnRaveResponseCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Button raveButtonPin = (Button) findViewById(R.id.rave_btn_auth_pin);
        Button raveButtonVbp = (Button) findViewById(R.id.rave_btn_vbv_secure);
        Button raveButtonNoAuth = (Button) findViewById(R.id.rave_btn_no_auth);
        Button raveButtonRandom = (Button) findViewById(R.id.rave_btn_random_debit);


//         vbvssecure
//
        raveButtonVbp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke",
                        "Shawarma and Coke for kenny",
                        1400.00,
                        "FLWPUBK-31e8501f7a46845633693ddcb42b25f8-X",//"FLWPUBK-6c011cdb12f0c81e332424b061047e7b-X",
                        "FLWSECK-0ad3beb5bf9f7708c8e4bab43bd4af0d-X",//"FLWSECK-9796e060383fecbe90f984dfa9e2e1df-X",
                        "jokafor@pacentltd.com",
                        "rave-checkout-1489769562",//"rave-checkout-1485623235",
                        RaveAuthModel.VBV_SECURECODE)
                        .withFirstName("Jerry")
                        .withLastName("Okafor")
                        .withNarration("This is a test Payment")
                        .build();
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData, MainActivity.this);
                rave.show();
            }
        });

        //no auth

        raveButtonNoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke",
                        "Shawarma and Coke for kenny",
                        1400.00,
                        "FLWPUBK-31e8501f7a46845633693ddcb42b25f8-X",//"FLWPUBK-6c011cdb12f0c81e332424b061047e7b-X",
                        "FLWSECK-0ad3beb5bf9f7708c8e4bab43bd4af0d-X",//"FLWSECK-9796e060383fecbe90f984dfa9e2e1df-X",
                        "jokafor@pacentltd.com",
                        "rave-checkout-1489769562",//"rave-checkout-1485623235",
                        RaveAuthModel.NOAUTH)
                        .withFirstName("Jerry")
                        .withLastName("Okafor")
                        .withNarration("This is a test Payment")
                        .build();
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData, MainActivity.this);
                rave.show();
            }
        });

//        // random debit
//
        raveButtonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke",
                        "Shawarma and Coke for kenny",
                        10000.0,
                        "FLWPUBK-31e8501f7a46845633693ddcb42b25f8-X",//"FLWPUBK-6c011cdb12f0c81e332424b061047e7b-X",
                        "FLWSECK-0ad3beb5bf9f7708c8e4bab43bd4af0d-X",//"FLWSECK-9796e060383fecbe90f984dfa9e2e1df-X",
                        "jokafor@pacentltd.com",
                        "rave-checkout-1489769562",//"rave-checkout-1485623235",
                        RaveAuthModel.RANDOM_DEBIT)
                        .withFirstName("Jerry")
                        .withLastName("Okafor")
                        .withNarration("This is a test Payment")
                        .build();
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData, MainActivity.this);
                rave.show();
            }
        });


// pin
        raveButtonPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke", //Comapany Name
                        "Shawarma and Coke for kenny",
                        2500.0,
                        "FLWPUBK-31e8501f7a46845633693ddcb42b25f8-X",//"FLWPUBK-6c011cdb12f0c81e332424b061047e7b-X",
                        "FLWSECK-0ad3beb5bf9f7708c8e4bab43bd4af0d-X",//"FLWSECK-9796e060383fecbe90f984dfa9e2e1df-X",
                        "jokafor@pacentltd.com",
                        RaveUtil.getTxRef(), // You can also use the RaveUtil Static Method
                        RaveAuthModel.PIN)
                        .withFirstName("Jerry")
                        .withLastName("Okafor")
                        .withNarration("This is a test Payment")
                        .build();
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData, MainActivity.this);
                rave.show();
            }
        });


    }


    @Override
    public void onResponse(RaveResponse response) {
        Log.d(TAG, response.toString());
    }
}

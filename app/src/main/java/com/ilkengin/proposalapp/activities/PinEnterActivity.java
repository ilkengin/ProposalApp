package com.ilkengin.proposalapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.goodiebag.pinview.Pinview;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.Utils;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

import java.util.Locale;

public class PinEnterActivity extends AppCompatActivity {

    private static final String TAG = PinEnterActivity.class.getSimpleName();

    private Pinview pinView;

    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_enter);

        pinView = (Pinview) findViewById(R.id.pin_view);


        Bundle args = getIntent().getExtras();

        if(args != null) {
            pin = args.getString(Constants.ARG_PIN,"");
        }

        pinView.setPinLength(pin.length());

        pinView.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                onPinEntered(pinview.getValue());
            }
        });
    }

    public void onPinEntered(String s) {
        Locale.setDefault(new Locale("tr"));
        Log.d(TAG,"onPinEntered. Pin is " + s);
        if(pin.toLowerCase().equals(s.toLowerCase())) {
            Log.d(TAG,"Pin is right");

            Utils.hideKeyboard(PinEnterActivity.this);

            Alerter.create(this)
                    .setTitle("Tebrikler")
                    .setText("Pin doğru!")
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setDuration(1000)
                    .setOnHideListener(new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            pinView.setValue("");
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    })
                    .show();
        } else {
            Log.d(TAG,"Pin is wrong");

            Utils.hideKeyboard(PinEnterActivity.this);

            Alerter.create(this)
                    .setTitle("Üzgünüm")
                    .setText("Pin hatalı!")
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setDuration(1000)
                    .setOnHideListener(new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            pinView.setValue("");
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        super.onBackPressed();
    }
}

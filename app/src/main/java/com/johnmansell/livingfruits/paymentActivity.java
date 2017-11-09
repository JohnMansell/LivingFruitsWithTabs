package com.johnmansell.livingfruits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.livingfruitswithtabs.R;
import com.stripe.android.view.CardInputWidget;

public class paymentActivity extends AppCompatActivity {

    CardInputWidget myCardInputWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        myCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
    }

}

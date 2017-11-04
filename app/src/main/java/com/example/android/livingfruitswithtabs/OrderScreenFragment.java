package com.example.android.livingfruitswithtabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by JOHNM on 10/10/2017.
 */

public class OrderScreenFragment extends android.support.v4.app.Fragment {

    //-------------------------------------------
    //  Declare Variables
    //===========================================

        // Numbers
            double subTotal = 0.0;
            double shipping = 0.0;
            double total = 0.0;
            int count125 = 0;
            int count400 = 0;
            int count600 = 0;
            boolean delivery = true;

        // Buttons
            RadioButton freeDeliveryButton;
            RadioButton standardDeliveryButton;
            RadioButton pickupButton;
            Button orderButton;


    //-------------------------------------------
    //  New Instance
    //===========================================
        public static OrderScreenFragment newInstance() {
            OrderScreenFragment myFragment = new OrderScreenFragment();
            return myFragment;
        }

    //-------------------------------------------
    //  On Create View
    //===========================================
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the Layout for this fragment
            View myView = inflater.inflate(R.layout.fragment_order, container, false);

            RadioGroup deliveryGroup = (RadioGroup) myView.findViewById(R.id.radioGroup);
            deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // Checked ID is the radio button selected

                    switch (checkedId) {
                        case R.id.pickup_radio_button:
                            subTotal = 0;
                            updatePrice();
                            break;

                        case R.id.free_delivery_radio:
                                break;

                        case R.id.standard_delivery_radio:
                                break;
                    }
                }
            });

            return myView;
        }

    //-------------------------------------------
    //  On View Created
    //===========================================
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Declare Variables:
            TextView signInText = (TextView) view.findViewById(R.id.sign_in_text);
            TextView coverText = (TextView) view.findViewById(R.id.order_cover_text);
            final EditText text125 = (EditText) view.findViewById(R.id.editText_125);
            final EditText text400 = (EditText) view.findViewById(R.id.edit_text_400);
            final EditText text600 = (EditText) view.findViewById(R.id.edit_text_600);
            freeDeliveryButton = (RadioButton) view.findViewById(R.id.free_delivery_radio);
            standardDeliveryButton = (RadioButton) view.findViewById(R.id.standard_delivery_radio);
            pickupButton = (RadioButton) view.findViewById(R.id.pickup_radio_button);
            orderButton = (Button) view.findViewById(R.id.continueButton1);
            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail();
                }
            });

            // Set sign in text
            signInText.setText(R.string.sign_in);

            // Set Cover photo text
            String spanCoverText = "Farm-direct, spray-free \nblueberries";
            SpannableString mySpanable = new SpannableString(spanCoverText);
            mySpanable.setSpan(new RelativeSizeSpan(2f), 24,36, 0);
            coverText.setText(mySpanable);


            // 125 grams -- $ 4.00
            final View button_1 = view.findViewById(R.id.icon125);
            button_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTotal += 4.00;
                    updatePrice();

                    count125 += 1;
                    updateCount(count125, text125);
                }
            });

            // 400 grams -- $12.00
            final View button_2 = view.findViewById(R.id.icon_400);
            button_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTotal += 12.00;
                    updatePrice();

                    count400 += 1;
                    updateCount(count400, text400);
                }
            });

            // 600 grams -- $16.00
            final View button_3 = view.findViewById(R.id.icon_600);
            button_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTotal += 16.00;
                    updatePrice();

                    count600 += 1;
                    updateCount(count600, text600);
                }
            });

        }


//==============================================================
//      Function Definitions
//==============================================================

    //-------------------------------------------
    //  Update Price
    //===========================================
        public void updatePrice(){
            total = subTotal + shipping;
            TextView price_Number = (TextView) getView().findViewById(R.id.subtotal_price);
            String price_string = String.format(Locale.ENGLISH, "$ %.2f", total);
            price_Number.setText(price_string);
        }

    //-------------------------------------------
    //  Update Count
    //===========================================
        public void updateCount(int count, EditText e){
        e.setText(String.valueOf(count));
    }

    //-------------------------------------------
    //  Order Button Create Intent
    //===========================================
    protected void sendEmail(){
        Log.i("Send email", "");
        String[] TO = {"info@livingfruits.co.nz"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        StringBuilder messageString = new StringBuilder();
        messageString.append("BlueBerries Order:\n\n");
        messageString.append("BlueBerry Ammounts: \n\n125: -- ");
        messageString.append(count125);
        messageString.append("\n400: -- ");
        messageString.append(count400);
        messageString.append("\n600: -- ");
        messageString.append(count600);
        messageString.append("\n\n\nSubtotal: ");
        messageString.append(subTotal);
        messageString.append("\nShipping: ");
        messageString.append(shipping);
        messageString.append("\nTotal: ");
        messageString.append(total);
        String myMessage = messageString.toString();


        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BlueBerry Order");
        emailIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
        startActivity(emailIntent);
    }








}
















































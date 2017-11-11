package com.livingfruits.android.livingfruitswithtabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by JOHNM on 10/10/2017.
 */

public class OrderScreenFragment extends android.support.v4.app.Fragment {

    //ToDo: Get all the cover texts to be the right size
    //-------------------------------------------
    //  Declare Variables
    //===========================================

        // Prices
            double price125 = 4.00;
            double price400 = 12.00;
            double price600 = 16.00;

        // Numbers
            double subTotal = 0.0;
            double shipping = 0.0;
            double total = 0.0;
            int count125 = 0;
            int count400 = 0;
            int count600 = 0;
            int delivery = 0;
            int toMove = 40;

        // Buttons
            RadioButton freeDeliveryButton;
            RadioButton standardDeliveryButton;
            RadioButton pickupButton;
            RadioGroup stdDeliveryGroup;
            Button orderButton;

        // Text View
            TextView shippingText;
            TextView shippingNumber;
            TextView totalText;
            TextView totalNumber;
            TextView subtotalNumber;
            TextView subtotalText;

        // Edit Texts
            EditText text125;
            EditText text400;
            EditText text600;

        // Delivery Constants
            int NONE = 0;
            int PICKUP = 1;
            int CBD_DELIVERY = 2;
            int STD_DELIVERY_NORTH_OF_AUCLKAND = 3;
            int STD_DELIVERY_WARKWORTH_NORTH = 4;
            int STD_DELIVERY_AUCKLAND = 5;
            int STD_DELIVERY_NZ = 6;
            int STD_DELIVERY = 0;
            String deliveryType = "";

        // Image View
            ImageView orderScreenLogo;

        // Constraint Layout
            ConstraintLayout constraintLayout;
            ConstraintSet set;


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

            // Radio Groups
                RadioGroup deliveryGroup = (RadioGroup) myView.findViewById(R.id.radioGroup);
                stdDeliveryGroup = (RadioGroup) myView.findViewById(R.id.std_delivery_group);
                deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // Checked ID is the radio button selected

                        switch (checkedId) {
                            case R.id.pickup_radio_button:
                                shipping = 0;
                                delivery = PICKUP;
                                deliveryType = "Pick-Up";
                                updatePrice();
                                stdDeliveryGroup.setVisibility(View.INVISIBLE);
                                break;

                            case R.id.free_delivery_radio:
                                shipping = 0;
                                delivery = CBD_DELIVERY;
                                deliveryType = "CBD - Delivery";
                                updatePrice();
                                stdDeliveryGroup.setVisibility(View.INVISIBLE);
                                break;

                            case R.id.standard_delivery_radio:
                                // ToDo: Impliment delivery charges if delivering outside of CBD
                                delivery = STD_DELIVERY;
                                deliveryType = "Standard Delivery";
                                stdDeliveryGroup.setVisibility(View.VISIBLE);

                                break;
                        }

                        if (stdDeliveryGroup.getVisibility() == View.VISIBLE) {
                            toMove = (stdDeliveryGroup.getHeight() + 80);
                        }

                        if (stdDeliveryGroup.getVisibility() == View.INVISIBLE) {
                            toMove = 0;}

                            shippingNumber.animate().translationY(toMove);
                            shippingText.animate().translationY(toMove);
                            orderButton.animate().translationY(toMove);
                            totalText.animate().translationY(toMove);
                            totalNumber.animate().translationY(toMove);
                            orderScreenLogo.animate().translationY(toMove);

                    }
                });

            stdDeliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // Checked ID is the radio button selected

                    switch (checkedId) {
                        case R.id.std_delivery_1:
                            // North of Auckland
                            delivery = STD_DELIVERY_NORTH_OF_AUCLKAND;
                            shipping = 0.0;
                            break;

                        case R.id.std_delivery_2:
                            // Warkworth North
                            delivery = STD_DELIVERY_WARKWORTH_NORTH;
                            shipping = 9.00;
                            break;

                        case R.id.std_delivry_3:
                            // Auckland
                            delivery = STD_DELIVERY_AUCKLAND;
                            shipping = 11.00;
                            break;

                        case R.id.std_delivry_4:
                            // Rest of New Zealand
                            delivery = STD_DELIVERY_NZ;
                            shipping = 18.00;
                            break;
                    }
                    updatePrice();

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

            //-------------------------
            //  Assign Views
            //-------------------------

            //Text Views
                TextView signInText = (TextView) view.findViewById(R.id.sign_in_text);
                TextView coverText = (TextView) view.findViewById(R.id.order_cover_text);
                shippingText = getView().findViewById(R.id.shippingText);
                shippingNumber = getView().findViewById(R.id.shippingPrice);
                totalText = getView().findViewById(R.id.totalText);
                totalNumber = getView().findViewById(R.id.totalPrice);
                subtotalNumber = getView().findViewById(R.id.subtotal_price);
                subtotalText = getView().findViewById(R.id.subtotal_text);

            //----------------------
            // Edit Text
            //----------------------

                // Edit Texts
                    text125 = (EditText) view.findViewById(R.id.editText_125);
                    text400 = (EditText) view.findViewById(R.id.edit_text_400);
                    text600 = (EditText) view.findViewById(R.id.edit_text_600);


                    View.OnFocusChangeListener editTextFocusListener = new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (!b) {

                                switch (view.getId()) {
                                    case R.id.editText_125:
                                        count125 = Integer.parseInt( text125.getText().toString());
                                        break;

                                    case R.id.edit_text_400:
                                        count400 = Integer.parseInt( text400.getText().toString());
                                        break;

                                    case R.id.edit_text_600:
                                        count600 = Integer.parseInt( text125.getText().toString());
                                        break;
                                }

                                updatePrice();
                            }
                        }
                    };

                    text125.setOnFocusChangeListener(editTextFocusListener);
                    text400.setOnFocusChangeListener(editTextFocusListener);
                    text600.setOnFocusChangeListener(editTextFocusListener);


            // Buttons
                freeDeliveryButton = (RadioButton) view.findViewById(R.id.free_delivery_radio);
                standardDeliveryButton = (RadioButton) view.findViewById(R.id.standard_delivery_radio);
                pickupButton = (RadioButton) view.findViewById(R.id.pickup_radio_button);
                orderButton = (Button) view.findViewById(R.id.continueButton1);
                orderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startPaymentActivity(view);
                    }
                });

            // Image View
                orderScreenLogo = view.findViewById(R.id.orderScreenLogo);


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
                        count125 += 1;
                        updateCount(count125, text125);
                        updatePrice();
                    }
                });

            // 400 grams -- $12.00
                final View button_2 = view.findViewById(R.id.icon_400);
                button_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count400 += 1;
                        updateCount(count400, text400);
                        updatePrice();
                    }
                });

            // 600 grams -- $16.00
                final View button_3 = view.findViewById(R.id.icon_600);
                button_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count600 += 1;
                        updateCount(count600, text600);
                        updatePrice();
                    }
                });

            if (stdDeliveryGroup.getVisibility() == View.VISIBLE) {
                toMove = (stdDeliveryGroup.getHeight() + 80);
            }

            if (stdDeliveryGroup.getVisibility() == View.INVISIBLE) {
                toMove = 0;}

            shippingNumber.animate().translationY(toMove);
            shippingText.animate().translationY(toMove);
            orderButton.animate().translationY(toMove);
            totalText.animate().translationY(toMove);
            totalNumber.animate().translationY(toMove);
            orderScreenLogo.animate().translationY(toMove);

        }
    //-------------------------------------------
    //  End On View Created
    //===========================================
    @Override
    public void onStart() {
            super.onStart();
        if (stdDeliveryGroup.getVisibility() == View.VISIBLE) {
            toMove = (stdDeliveryGroup.getHeight() + 80);
        }

        if (stdDeliveryGroup.getVisibility() == View.INVISIBLE) {
            toMove = 0;}

        shippingNumber.animate().translationY(toMove);
        shippingText.animate().translationY(toMove);
        orderButton.animate().translationY(toMove);
        totalText.animate().translationY(toMove);
        totalNumber.animate().translationY(toMove);
        orderScreenLogo.animate().translationY(toMove);
    }



//==============================================================
//      Function Definitions
//==============================================================

    //-------------------------------------------
    //  Update Price
    //===========================================
        public void updatePrice(){
            subTotal = (count125 * price125) + (count400 * price400) + (count600 * price600);

            total = subTotal + shipping;

            // Subtotal
            String price_string = String.format(Locale.ENGLISH, "$ %.2f", total);
            subtotalNumber.setText(price_string);

            // Shipping
            String shippingTextSTring = String.format(Locale.ENGLISH, "$ %.2f", shipping);
            shippingNumber.setText(shippingTextSTring);

            // Total
            String totalTextString = String.format(Locale.ENGLISH, "$ %.2f", total);
            totalNumber.setText(totalTextString);
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

        public void startPaymentActivity(View view) {

            // Check For Delivery Method:
                if (delivery == NONE) {
                    Toast.makeText(getActivity(), "Please Choose a Delivery Method", Toast.LENGTH_SHORT).show();
                    return;
                }

            if ((count125 == 0) && (count400 == 0) && (count600 == 0)) {
                Toast.makeText(getActivity(), "Your order has zero blueberries", Toast.LENGTH_SHORT).show();
                return;
            }


            // Build Intent
                    Intent paymentIntent = new Intent(getContext(), paymentActivity.class);

                    Bundle intentBundle = new Bundle();
                    intentBundle.putDouble("subTotal", subTotal);
                    intentBundle.putDouble("shipping", shipping);
                    intentBundle.putDouble("total", total);
                    intentBundle.putInt("count125", count125);
                    intentBundle.putInt("count400", count400);
                    intentBundle.putInt("count600", count600);
                    intentBundle.putInt("delivery", delivery);
                    paymentIntent.putExtras(intentBundle);

            // Start Activity
                    startActivity(paymentIntent);
    }




}
















































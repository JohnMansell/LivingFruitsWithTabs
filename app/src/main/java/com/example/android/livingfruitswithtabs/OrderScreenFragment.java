package com.example.android.livingfruitswithtabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by JOHNM on 10/10/2017.
 */

public class OrderScreenFragment extends android.support.v4.app.Fragment {

    //-------------------------------------------
    //  Declare Variables
    //===========================================
    double price = 0.0;
    int count125 = 0;
    int count400 = 0;
    int count600 = 0;

    boolean delivery = true;
    ImageView deliveryIcon;
    RadioButton freeDeliveryButton;
    RadioButton standardDeliveryButton;
    RadioButton pickupButton;

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
        return inflater.inflate(R.layout.fragment_order, container, false);
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

        // Set sign in text
        signInText.setText(R.string.sign_in);

        // Set Cover photo text
        String spanCoverText = "Farm-direct, spray-free \nblueberries";
        SpannableString mySpanable = new SpannableString(spanCoverText);
        mySpanable.setSpan(new RelativeSizeSpan(2f), 24,36, 0);
        coverText.setText(mySpanable);


        // 125 grams -- $ 4.00
        final View button_1 = view.findViewById(R.id.icon_125);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price += 4.00;
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
                price += 12.00;
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
                price += 16.00;
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
        TextView price_Number = (TextView) getView().findViewById(R.id.subtotal_price);
        String price_string = String.format(Locale.ENGLISH, "$ %.2f", price);
        price_Number.setText(price_string);
    }

    //-------------------------------------------
    //  Update Count
    //===========================================
    public void updateCount(int count, EditText e){
        e.setText(String.valueOf(count));
    }
}

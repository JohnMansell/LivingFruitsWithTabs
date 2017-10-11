package com.example.android.livingfruitswithtabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
    RadioButton deliveryButton;
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
        final EditText text125 = (EditText) view.findViewById(R.id.count_125);
        final EditText text400 = (EditText) view.findViewById(R.id.count_400);
        final EditText text600 = (EditText) view.findViewById(R.id.count_600);
        deliveryIcon = (ImageView) view.findViewById(R.id.deliveryIcon);
        deliveryButton = (RadioButton) view.findViewById(R.id.deliveryButton);
        pickupButton = (RadioButton) view.findViewById(R.id.pickupButton);

        // 125 grams -- $ 4.00
        final View button_1 = view.findViewById(R.id.blueberry_125);
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
        final View button_2 = view.findViewById(R.id.blueberry_400);
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
        final View button_3 = view.findViewById(R.id.blueberry_600);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price += 16.00;
                updatePrice();

                count600 += 1;
                updateCount(count600, text600);
            }
        });

        // Clear
        final View clear_button = view.findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear Prices
                price = 0.00;
                updatePrice();

                // Clear Counts
                count125 = 0;
                count400 = 0;
                count600 = 0;
                updateCount(count125, text125);
                updateCount(count400, text400);
                updateCount(count600, text600);
            }
        });

        final ImageView deliveryIcon = (ImageView) view.findViewById(R.id.deliveryIcon);
        deliveryIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Delivery
        deliveryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle Delivery
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
        TextView price_Number = (TextView) getView().findViewById(R.id.price_number);
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

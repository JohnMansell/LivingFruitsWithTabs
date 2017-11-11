package com.livingfruits.android.livingfruitswithtabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class contact_fragment extends Fragment {
    //-------------------------------------------
    //      Declare Variables
    //===========================================

        // Text Views
            TextView landline;
            TextView mobileNumber;
            TextView emailAddress;

        // Icons
            ImageView land_line_icon;
            ImageView mobile_icon;

        // Buttons
            Button google_maps_button;

        // Phone Numbers
            String land_line_string = "+64-09-407-6880";
            String mobile_number_string = "+64-021-171-6061";
            String email_string = "info@livingFruits.co.nz";
            String latitude = "-35.185002";
            String longitude = "173.931100";
            Uri gmmIntentUri = Uri.parse("geo:-35.185002, 173.931100");


        // On Click Listener
            View.OnClickListener phone_click_listener;


    //-------------------------------------------
    //      Default Constructor
    //===========================================
        public contact_fragment() {
            // Required empty public constructor
        }

    //-------------------------------------------
    //      New Instance
    //===========================================
        public static contact_fragment newInstance() {
            contact_fragment fragment = new contact_fragment();
            return fragment;
        }

    //-------------------------------------------
    //      On Create
    //===========================================
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


    //-------------------------------------------
    //      On Create View
    //===========================================
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Inflate the Layout for this fragment
                View myView = inflater.inflate(R.layout.fragment_contact, container, false);

                phone_click_listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (view.getId()) {
                            case R.id.landLine:
                            case R.id.phoneIcon_land:
                                call_phone(land_line_string);
                                break;

                            case R.id.mobilePhone:
                            case R.id.phoneIcon_mobile:
                                call_phone(mobile_number_string);
                                break;

                        }
                    }
                };

                return myView;
        }

    //-------------------------------------------
    //      On View Created
    //===========================================
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Assign Views
                landline = view.findViewById(R.id.landLine);
                mobileNumber = view.findViewById(R.id.mobilePhone);
                land_line_icon = view.findViewById(R.id.phoneIcon_land);
                mobile_icon = view.findViewById(R.id.phoneIcon_mobile);
                google_maps_button = view.findViewById(R.id.googleMapButton);
                emailAddress = view.findViewById(R.id.emailAddress);

            // Set On Click Listeners
                landline.setOnClickListener(phone_click_listener);
                mobileNumber.setOnClickListener(phone_click_listener);
                land_line_icon.setOnClickListener(phone_click_listener);
                mobile_icon.setOnClickListener(phone_click_listener);

                emailAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + email_string));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, email_string);
                        if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivity(emailIntent);
                    }
                });

                google_maps_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent googleMapsIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        googleMapsIntent.setPackage("com.google.android.apps.maps");
                        if (googleMapsIntent.resolveActivity(getActivity().getPackageManager()) != null)
                            startActivity(googleMapsIntent);
                    }
                });

        }



    //-------------------------------------------
    //      Intents
    //===========================================

        // Mobile Phone
        public void call_phone(String phoneNumber) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(phoneIntent);
        }
}





















































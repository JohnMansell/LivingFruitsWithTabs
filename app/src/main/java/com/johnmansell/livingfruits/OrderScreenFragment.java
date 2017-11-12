package com.johnmansell.livingfruits;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.livingfruits.johnmansell.R;

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
            double price400 = 15.00;
            double price600 = 20.00;
            double price1000 = 32.00;

        // Numbers
            double subTotal = 0.0;
            double shipping = 0.0;
            double total = 0.0;
            int count400 = 0;
            int count600 = 0;
            int count1000 = 0;
            int delivery = 0;
            int toMove = 40;
            double total_weight = 0;
            private static final int RC_SIGN_IN = 9001;

        // Buttons
            RadioButton freeDeliveryButton;
            RadioButton standardDeliveryButton;
            RadioButton pickupButton;
            RadioGroup stdDeliveryGroup;
            Button orderButton;
            SignInButton googleSignInButton;

        // Text View
            TextView shippingText;
            TextView shippingNumber;
            TextView totalText;
            TextView totalNumber;
            TextView subtotalNumber;
            TextView subtotalText;
            TextView totalWeightNumber;
            TextView signInText;
            TextView cancel_signIn;
            TextView signOutText;

        // Edit Texts
            EditText text400;
            EditText text600;
            EditText text1000;

        // Delivery Constants
            int NONE = 0;
            int PICKUP = 1;
            int CBD_DELIVERY = 2;
            int STD_DELIVERY_NORTHLAND = 3;
            int STD_DELIVERY_AUCKLAND = 5;
            int STD_DELIVERY_NZ = 6;
            int STD_DELIVERY = 0;
            String deliveryType = "";

        // Image View
            ImageView orderScreenLogo;

        // Strings
            String email_string = "info@livingfruits.co.nz";
            private static final String TAG = "SignInActivity";

        // Google Sign In
            GoogleApiClient mGoogleApiClient;
            FrameLayout signInFrame;
            View.OnClickListener signInOutClickListener;
            private GoogleSignInClient mGoogleSignInClient;

        // Stripe


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

            //==============================
            //  Google Sign In
            //==============================

                // Sign In Options
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                // Google API Client
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                    }
                                })
                                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                                .build();
                    }

                // Google Sign In Client
                    mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);



            //-----------------------------
            //  Radio Groups
            //----------------------------
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

                        //-----------------------
                        //  Animation
                        //-----------------------
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

                //-----------------------------
                //  Std Delivery Group
                //-----------------------------
                    stdDeliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // Checked ID is the radio button selected

                            switch (checkedId) {
                                case R.id.std_delivery_1:
                                    // Northland
                                    delivery = STD_DELIVERY_NORTHLAND;
                                    deliveryType = "Std Delivery - Northland & Warkworth";
                                    shipping = 9.00;
                                    break;

                                case R.id.std_delivry_3:
                                    // Auckland
                                    delivery = STD_DELIVERY_AUCKLAND;
                                    deliveryType = "Std Delivery - Auckland";
                                    shipping = 11.00;
                                    break;

                                case R.id.std_delivry_4:
                                    // Rest of New Zealand
                                    delivery = STD_DELIVERY_NZ;
                                    deliveryType = "Std Delivery - NZ";
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
                signInText = (TextView) view.findViewById(R.id.sign_in_text);
                TextView coverText = (TextView) view.findViewById(R.id.order_cover_text);
                shippingText = getView().findViewById(R.id.shippingText);
                shippingNumber = getView().findViewById(R.id.shippingPrice);
                totalText = getView().findViewById(R.id.totalText);
                totalNumber = getView().findViewById(R.id.totalPrice);
                subtotalNumber = getView().findViewById(R.id.subtotal_price);
                subtotalText = getView().findViewById(R.id.subtotal_text);
                totalWeightNumber = getView().findViewById(R.id.total_weight_number);
                signInFrame = getView().findViewById(R.id.google_frame_layout);
                cancel_signIn = getView().findViewById(R.id.cancel_text);
                signOutText = getView().findViewById(R.id.sign_out_text);

            //----------------------
            // Edit Text
            //----------------------

                // Edit Texts
                    text400 = (EditText) view.findViewById(R.id.edit_text_400);
                    text600 = (EditText) view.findViewById(R.id.edit_text_600);
                    text1000 = (EditText) view.findViewById(R.id.edit_text_1000);


                    View.OnFocusChangeListener editTextFocusListener = new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (!b) {

                                switch (view.getId()) {

                                    case R.id.edit_text_400:
                                        count400 = Integer.parseInt( text400.getText().toString());
                                        break;

                                    case R.id.edit_text_600:
                                        count600 = Integer.parseInt( text600.getText().toString());
                                        break;

                                    case R.id.edit_text_1000:
                                        count1000 = Integer.parseInt( text1000.getText().toString());
                                }

                                updatePrice();
                            }
                        }
                    };

                    text400.setOnFocusChangeListener(editTextFocusListener);
                    text600.setOnFocusChangeListener(editTextFocusListener);
                    text1000.setOnFocusChangeListener(editTextFocusListener);


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

            // Sign In Button
                googleSignInButton = getView().findViewById(R.id.google_sign_in_button);
                googleSignInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signIn();
                    }
                });

            // Image View
                orderScreenLogo = view.findViewById(R.id.orderScreenLogo);


            // Set sign in text

                signInOutClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case (R.id.sign_in_text):
                                signInFrame.setVisibility(View.VISIBLE);
                                break;

                            case (R.id.cancel_text):
                                signInFrame.setVisibility(View.INVISIBLE);
                                break;

                            case (R.id.sign_out_text):
                                signOut();
                                break;
                        }
                    }
                };
                signInText.setText(R.string.sign_in);


                signInText.setOnClickListener(signInOutClickListener);
                cancel_signIn.setOnClickListener(signInOutClickListener);
                signOutText.setOnClickListener(signInOutClickListener);


            // Set Cover photo text
                String spanCoverText = "Farm-direct, spray-free \nblueberries";
                SpannableString mySpanable = new SpannableString(spanCoverText);
                mySpanable.setSpan(new RelativeSizeSpan(2f), 24,36, 0);
                coverText.setText(mySpanable);


            // 400 grams -- $12.00
                final View button_1 = view.findViewById(R.id.icon_400);
                button_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count400 += 1;
                        updateCount(count400, text400);
                        updatePrice();
                    }
                });

            // 600 grams -- $20.00
                final View button_2 = view.findViewById(R.id.icon_600);
                button_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count600 += 1;
                        updateCount(count600, text600);
                        updatePrice();
                    }
                });

            // 600 grams -- $32.00
            final View button_3 = view.findViewById(R.id.icon_1000);
            button_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count1000 += 1;
                    updateCount(count1000, text1000);
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

            //-------------------------------
            //  Set Correct Prices
            //-------------------------------
                TextView priceLabel_400 = view.findViewById(R.id.price_lable_400);
                TextView priceLabel_600 = view.findViewById(R.id.price_lable_600);
                TextView priceLabel_1000 = view.findViewById(R.id.price_lable_1000);

                String price400string = String.format(Locale.ENGLISH, "$ %.2f", price400);
                String price600string = String.format(Locale.ENGLISH, "$ %.2f", price600);
                String price1000string = String.format(Locale.ENGLISH, "$ %.2f", price1000);

                priceLabel_400.setText(price400string);
                priceLabel_600.setText(price600string);
                priceLabel_1000.setText(price1000string);

        }
    //-------------------------------------------
    //  End On View Created
    //===========================================

    //-------------------------------
    //  On Resume
    //-------------------------------
        @Override
        public void onResume() {
                super.onResume();
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

    //-------------------------------
    //  On Start
    //-------------------------------
    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
        // [END on_start_sign_in]
    }


//==============================================================
//      Function Definitions
//==============================================================

    //-------------------------------------------
    //  Sign In With Google
    //===========================================

        //----------------------
        //  Sign In
        //----------------------
            private void signIn() {

            Log.d(TAG, "Sign In Called");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        //----------------------
        //  Sign Out
        //----------------------
            private void signOut() {

            Log.d(TAG, "Sign Out Called");
                Toast.makeText(getContext(), "Sign Out", Toast.LENGTH_SHORT).show();
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateUI(null);
                        }
                    });
            }

        //----------------------
        //  On Activity Result
        //----------------------
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Log.d("MainActivity", "On Activity Result");

            Toast.makeText(getContext(), "On Activity Result", Toast.LENGTH_SHORT).show();

                    // Result returned from launching the Intent from Google Sign In Api.getSignInIntent();
                    if (requestCode == RC_SIGN_IN) {
                        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                        handleSignInResult(result);
                        Log.d("MainActivity", "Google Sign In Result = " + result);
                    }
        }

        //----------------------
        //  Handle Sign In Result
        //----------------------
            private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                updateUI(acct);
                Toast.makeText(getContext(), "Log In Success", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getContext(), "Log In Unsucessful", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

            signInFrame.setVisibility(View.INVISIBLE);
            }

        //----------------------
        //  Update UI
        //----------------------
            private void updateUI(@Nullable GoogleSignInAccount account) {
                if (account != null) {
                    signInText.setText("Hello, " + account.getDisplayName());
                    signInText.setTextColor(getResources().getColor(R.color.Burgundy));
                    signOutText.setVisibility(View.VISIBLE);
                } else {
                    signInText.setText(R.string.sign_in);
                    signOutText.setVisibility(View.INVISIBLE);
                }
                }
    //-------------------------------------------
    //  Update Price
    //===========================================
        public void updatePrice(){
            subTotal = (count400 * price400) + (count600 * price600) + (count1000 * price1000);
            total_weight = (count400 * 0.4) + (count600 * 0.6) + (count1000 * 1);
            String weightString = String.format(Locale.ENGLISH, "%.1f kg", total_weight);
            totalWeightNumber.setText(weightString);

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

            double totalWeight = (count400 * 0.4) + (count600 * 0.6) + (count1000 * 1.0);

            // Check For Delivery Method:
                if (delivery == NONE) {
                    Toast.makeText(getActivity(), "Please Choose a Delivery Method", Toast.LENGTH_SHORT).show();
                    return;
                }

            // Check for blueberry order
                if ((count1000 == 0) && (count400 == 0) && (count600 == 0)) {
                    Toast.makeText(getActivity(), "Your order has zero blueberries", Toast.LENGTH_SHORT).show();
                    return;
                }

            // Check Delivery Constriants
                if ((delivery == STD_DELIVERY_NORTHLAND) || (delivery == STD_DELIVERY_AUCKLAND)) {
                    if (totalWeight > 15.0) {
                        Toast.makeText(getActivity(), "Total Weight is over the standard delivery limit for this region. Please call Living Fruits from our contact page for custom delivery options", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            if (delivery == STD_DELIVERY_NZ) {
                if (totalWeight > 5.0) {
                    Toast.makeText(getActivity(), "Total Weight is over the standard delivery limit for this region. Please call Living Fruits from our contact page for custom delivery options", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            //==================================
            //      Email Intent
            //==================================

//                // Intent
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                    emailIntent.setData(Uri.parse("mailto:" + email_string));
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Blueberry Order");
//
//                // String Builder
//                    StringBuilder emailString = new StringBuilder();
//                    emailString.append("New Blueberry Order");
//                    emailString.append("\n-------------------------------------");
//                    emailString.append("\n\nTotal Weight = " + totalWeight + " kg");
//                    emailString.append("\n\n400g -- " + count400);
//                    emailString.append("\n600g -- " + count600);
//                    emailString.append("\n1 kg -- " + count1000);
//                    emailString.append("\n\nDelivery: " + deliveryType);
//                    emailString.append("\n\nSubtotal = " + subTotal);
//                    emailString.append("\nShipping = " + shipping);
//                    emailString.append("\nTotal = " + total);
//                    String email_body = emailString.toString();
//
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, email_body);
//
//                // Start Activity
//                    if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null)
//                        startActivity(emailIntent);

            //==================================
            //      Payment Intent
            //==================================

            //ToDo: Fix Payment Method
            // ToDo: Put sign in option back in

            // Build Intent
                    Intent paymentIntent = new Intent(getContext(), paymentActivity.class);

                    Bundle intentBundle = new Bundle();
                    intentBundle.putDouble("subTotal", subTotal);
                    intentBundle.putDouble("shipping", shipping);
                    intentBundle.putDouble("total", total);
                    intentBundle.putInt("count400", count400);
                    intentBundle.putInt("count600", count600);
                    intentBundle.putInt("count1000", count1000);
                    intentBundle.putInt("delivery", delivery);
                    paymentIntent.putExtras(intentBundle);

            // Start Activity
                    startActivity(paymentIntent);
    }

}
















































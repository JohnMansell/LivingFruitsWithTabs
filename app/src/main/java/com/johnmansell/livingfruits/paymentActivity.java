package com.johnmansell.livingfruits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.livingfruitswithtabs.R;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.util.Arrays;
import java.util.Locale;

public class paymentActivity extends AppCompatActivity {

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

        // Buttons

        // Text View
            TextView shippingNumber;
            TextView totalNumber;
            TextView subtotalNumber;
            TextView deliveryText;
            TextView quant125;
            TextView quant400;
            TextView quant600;

        // Delivery Constants
            static final int NONE = 0;
            static final int PICKUP = 1;
            static final int CBD_DELIVERY = 2;
            static final int STD_DELIVERY_NORTH_OF_AUCLKAND = 3;
            static final int STD_DELIVERY_WARKWORTH_NORTH = 4;
            static final int STD_DELIVERY_AUCKLAND = 5;
            static final int STD_DELIVERY_NZ = 6;
            String deliveryType = "";

        // Constraint Layout
            ConstraintLayout constraintLayout;
            ConstraintSet set;

        // Payment Variables
            CardInputWidget myCardInputWidget;
            PaymentsClient paymentsClient;
            private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 4;


    //===========================================
    //          On Create
    //===========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get Extras From Intent
            Bundle intentBundle = getIntent().getExtras();
            subTotal = intentBundle.getDouble("subTotal");
            shipping = intentBundle.getDouble("shipping");
            total = intentBundle.getDouble("total");
            count125 = intentBundle.getInt("count125");
            count400 = intentBundle.getInt("count400");
            count600 = intentBundle.getInt("count600");
            delivery = intentBundle.getInt("delivery");

        // Assign Text Views
            shippingNumber = findViewById(R.id.summary_shipping_number);
            totalNumber = findViewById(R.id.summary_total_number);
            subtotalNumber = findViewById(R.id.summary_subtotal_numberl);
            deliveryText = findViewById(R.id.summary_address_text);

            quant125 = findViewById(R.id.summary_count125);
            quant400 = findViewById(R.id.summary_count400);
            quant600 = findViewById(R.id.summary_count600);

        // Assign Values to text Views
            String subtotal_string = String.format(Locale.ENGLISH, "$ %.2f", subTotal);
            subtotalNumber.setText(subtotal_string);

            String shipping_string = String.format(Locale.ENGLISH, "$ %.2f", shipping);
            shippingNumber.setText(shipping_string);

            String total_string = String.format(Locale.ENGLISH, "$ %.2f", total);
            totalNumber.setText(total_string);

            deliveryType = getDeliveryType(delivery);
            deliveryText.setText(deliveryType);

            quant125.setText("" + count125);
            quant400.setText("" + count400);
            quant600.setText("" + count600);


        // Payments Client
            paymentsClient =
                    Wallet.getPaymentsClient(this, new Wallet.WalletOptions.Builder().setEnvironment(
                            WalletConstants.ENVIRONMENT_TEST).build());


//
//        Card cardToSave = myCardInputWidget.getCard();
//        if (cardToSave == null) {
//            // Todo: Handle card to save == Null;
//        }

        Button buyButton = findViewById(R.id.buyButton);
        buyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PaymentDataRequest request = createPaymentDataRequest();
                        if (request != null) {
                            AutoResolveHelper.resolveTask(
                                    paymentsClient.loadPaymentData(request),
                                    paymentActivity.this,
                                    LOAD_PAYMENT_DATA_REQUEST_CODE);
                        }
                    }
                }
        );

        } // End On create
    //-------------------------------


    //------------------------------
    //  Create Delivery String
    //------------------------------
        private String getDeliveryType(int deliveryOption) {

        String myDeliveryOption = "";
            switch (deliveryOption) {
                case NONE:
                    myDeliveryOption = "Invalid Delivery Option";
                    break;

                case PICKUP:
                    myDeliveryOption = "Pick Up at Living Fruits Packhouse";
                    break;

                case CBD_DELIVERY:
                    myDeliveryOption = "FREE - Delivery to Kerikeri CBD";
                    break;

                case STD_DELIVERY_NORTH_OF_AUCLKAND:
                    myDeliveryOption = "Standard Delivery - North of Auckland";
                    break;

                case STD_DELIVERY_WARKWORTH_NORTH:
                    myDeliveryOption = "Standard Delivery - Warkworth North";
                    break;

                case STD_DELIVERY_AUCKLAND:
                    myDeliveryOption = "Standard Delivery - Auckland";
                    break;

                case STD_DELIVERY_NZ:
                    myDeliveryOption = "Standard Delivery - NZ";
                    break;
            }

            return myDeliveryOption;
        }

    //------------------------------
    //  Is Ready to Pay
    //------------------------------
    private void isReadyToPay() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(APIException.class);
                            if (result == true) {
                                // TODO: show google payment as option
                            }
                            else {
                                // TODO: Hide google payment as option
                            }
                        } catch (APIException exception) {// ToDO: Handle exception
                        }
                    }
                });
    }

    //------------------------------
    //  Payment Method Tokenization Parameters
    //------------------------------
    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", "pk_test_JSFDMqFbP1m2FSrlQE4qSBFY")
                .addParameter("stripe:version", "5.1.1")
                .build();
    }


    //------------------------------
    //  Payment Data Request
    //------------------------------
    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                .setTransactionInfo(
                        TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setTotalPrice("10.00")
                        .setCurrencyCode("USD")
                        .build())
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .setCardRequirements(
                        CardRequirements.newBuilder()
                        .addAllowedCardNetworks(Arrays.asList(
                                WalletConstants.CARD_NETWORK_AMEX,
                                WalletConstants.CARD_NETWORK_DISCOVER,
                                WalletConstants.CARD_NETWORK_VISA,
                                WalletConstants.CARD_NETWORK_MASTERCARD
                        )).build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    //------------------------------
    //  On Activity Result
    //------------------------------
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case LOAD_PAYMENT_DATA_REQUEST_CODE:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            PaymentData paymentData = PaymentData.getFromIntent(data);

                            CardInfo info = paymentData.getCardInfo();
                                // ToDo: you can get some data on the user's card, such as the brand and the last 4 digits.
                            UserAddress address = paymentData.getShippingAddress();
                                // ToDo: you can also pull the user's address from the Payment Data Object.
                            String rawToken = paymentData.getPaymentMethodToken().getToken();
                                // ToDo: this is the raw JSON string versoin of your stipe Token

                            // Now that you have a Stripe Token object, charge that by using the id
                            Token stripeToken = Token.fromString(rawToken);
                            if (stripeToken != null) {
                                // ToDO: This chargeToken function is a call to your own server, which should then
                                // ToDo: connect to stripe's API to finish the charge.
                                //chargeToken(stipeToken.getID());
                            }
                            break;

                        case Activity.RESULT_CANCELED:
                            break;

                        case AutoResolveHelper.RESULT_ERROR:
                            // ToDO: Log Status error
                            // Status status = AutoResolveHelper.getStatusFromIntent(data);
                            break;

                        default:
                            // Do Nothing.
                    }
            }
        }


}





















































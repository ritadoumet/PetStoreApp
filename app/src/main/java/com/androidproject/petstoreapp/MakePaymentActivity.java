package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MakePaymentActivity extends AppCompatActivity {

    TextView paymentAmount;
    PaymentType paymentType;
    RadioGroup paymentTypeRadioGroup;
    LinearLayout payPalLayout;
    LinearLayout cardLayout;
    EditText payPalEmail;
    EditText payPalPass;
    EditText cardNumber;
    EditText CVVNumber;
    Button finishPaymentBtn;
    TextView username;
    Boolean paymentMade;
    TextView logout;
    AlertDialog logoutAlert;
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        String purpose = null;
        paymentMade=false;
        paymentAmount = findViewById(R.id.paymtAmtVal);
        paymentTypeRadioGroup = findViewById(R.id.radioPaymentType);
        payPalLayout = findViewById(R.id.byPayPalLayout);
        cardLayout = findViewById(R.id.byCardLayout);
        payPalEmail = findViewById(R.id.payPalEmail);
        username = findViewById(R.id.username);
        payPalPass = findViewById(R.id.payPalPass);
        cardNumber = findViewById(R.id.cardNumber);
        CVVNumber = findViewById(R.id.cardCVV);
        finishPaymentBtn = findViewById(R.id.finishPayment);

        logout = findViewById(R.id.logout);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());
        double paymentAmountValue = 0.0;
        int userID = 0;
        int bookingID = 0;
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                paymentAmountValue=0.0;
            }
            else {
                paymentAmountValue=extras.getDouble("paymentAmount");
                userID = extras.getInt("userID");
                purpose = extras.getString("purpose");
                Intent intent = getIntent();
                if (intent.hasExtra("bookingID"))
                    bookingID = extras.getInt("bookingID");

                paymentAmount.setText("$"+paymentAmountValue+"");
            }
        }

        paymentTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.payPal){
                cardLayout.setVisibility(View.GONE);
                payPalLayout.setVisibility(View.VISIBLE);
                paymentType = PaymentType.PayPal;
            }
            else{
                cardLayout.setVisibility(View.VISIBLE);
                payPalLayout.setVisibility(View.GONE);
                paymentType = PaymentType.Card;
            }
        });

        double finalPaymentAmountValue = paymentAmountValue;
        int finalUserID = userID;
        String finalPurpose = purpose;
        int finalBookingID = bookingID;
        finishPaymentBtn.setOnClickListener(v -> {
            if (!paymentMade){
                if (validInput()){
                    paymentMade=true;
                    Payment payment = new Payment(paymentType, finalPaymentAmountValue, finalUserID);
                    Payment.insertPayment(payment);
                    if (finalPurpose.equals("buyCart")){
                        Owner o = (Owner) User.getCurrentUser();
                        o.Cart = new HashMap<>();
                        Intent i = new Intent(MakePaymentActivity.this, ConfirmationActivity.class);
                        i.putExtra("confirmation_msg", "Payment for $"+finalPaymentAmountValue+" has been made on your account.");
                        startActivity(i);
                        finish();
                    }
                    // if final purpose = bookApt

                    if (finalPurpose.equals("bookApt")){
                        Booking.completeBooking(finalBookingID);
                        Intent i = new Intent(MakePaymentActivity.this, ConfirmationActivity.class);
                        i.putExtra("confirmation_msg", "Payment for $"+finalPaymentAmountValue+" has been made on the client's account");
                        startActivity(i);
                        finish();
                    }

                }
            }

        });

    }



    private boolean validInput() {
        if (paymentType.equals(PaymentType.PayPal)){
            if (payPalEmail.getText().toString().equals("") || !payPalEmail.getText().toString().contains("@") || (!payPalEmail.getText().toString().contains(".com") && !payPalEmail.getText().toString().contains(".org") && !payPalEmail.getText().toString().contains(".edu"))  ){
                Toast.makeText(this, "Please enter valid paypal email", Toast.LENGTH_LONG).show();
                return false;
            }
            if (payPalPass.getText().toString().equals("")){
                Toast.makeText(this, "Please enter valid paypal password", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                return true;
            }
        }
        else{
            if (cardNumber.getText().toString().equals("") ){
                Toast.makeText(this, "Please enter valid card number", Toast.LENGTH_LONG).show();
                return false;
            }
            if (CVVNumber.getText().toString().length()!=4){
                Toast.makeText(this, "Please enter valid card CVV number", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }

    private void logoutDialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);

        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dlg, which) -> logout());
        builder.setNegativeButton("Cancel", (dialog, which) -> logoutAlert.cancel());

        logoutAlert= builder.create();
        logoutAlert.show();
    }

    private void logout() {
        User.setCurrentUser(null);
        Intent i = new Intent(MakePaymentActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
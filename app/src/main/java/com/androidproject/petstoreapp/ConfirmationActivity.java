package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    TextView confirmation_msg_text;
    Button goBackButton;

    String msg;
    AlertDialog logoutAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> logoutDialog());
        username= findViewById(R.id.username);
        username.setText(User.getCurrentUser().getUsername());
        confirmation_msg_text = findViewById(R.id.confirmation_message_text);
        goBackButton = findViewById(R.id.go_back_btn);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
               msg= "";
            }
            else {
                msg=extras.getString("confirmation_msg");
            }
        }

        confirmation_msg_text.setText(msg);
        goBackButton.setOnClickListener(v -> finish());
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
        Intent i = new Intent(ConfirmationActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
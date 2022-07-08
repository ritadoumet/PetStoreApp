package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainOwnerActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    Button viewMyPetsBtn;
    Button addAPetBtn;
    Button bookApptBtn;
    Button cancelApptBtn;
    Button browseProductsBtn;
    Button viewMyCartBtn;
    Button emergencyCallBtn;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        Owner o = (Owner) User.getCurrentUser();
        o.loadData();
        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        viewMyPetsBtn = findViewById(R.id.viewMyPetsBtn);
        addAPetBtn = findViewById(R.id.addAPetBtn);
        bookApptBtn = findViewById(R.id.bookApptBtn);
        cancelApptBtn = findViewById(R.id.cancelApptBtn);
        browseProductsBtn = findViewById(R.id.browseProductsBtn);
        viewMyCartBtn = findViewById(R.id.viewMyCartBtn);
        emergencyCallBtn = findViewById(R.id.emergencyCallBtn);

        setUsername();

        logout.setOnClickListener(v -> logoutDialog());

        addAPetBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, AddAPetActivity.class);
            startActivity(i);
        });

        viewMyPetsBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, ViewMyPetsActivity.class);
            startActivity(i);
        });
        bookApptBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, BookApptActivity.class);
            startActivity(i);
        });

        cancelApptBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, ViewApptsActivity.class);
            startActivity(i);
        });


        browseProductsBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, BrowseProductsActivity.class);
            startActivity(i);
        });

        viewMyCartBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainOwnerActivity.this, ViewMyCartActivity.class);
            startActivity(i);
        });
        emergencyCallBtn.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+96170119630"));//change the number.
            startActivity(callIntent);
        });
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
        Intent i = new Intent(MainOwnerActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setUsername() {
        username.setText(User.getCurrentUser().getUsername());
    }


}
package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainEmployeeActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    Button logAppt;
    Button viewSchedule;
    Button bookAppt;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);


        Employee emp = (Employee) User.getCurrentUser();
        emp.loadData();

        username= findViewById(R.id.username);
        logout=findViewById(R.id.logout);
        logAppt=findViewById(R.id.logAppt);
        viewSchedule=findViewById(R.id.viewSchedule);
        username.setText(User.getCurrentUser().getUsername());
        bookAppt = findViewById(R.id.bookAppt);

        logout.setOnClickListener(v -> logoutDialog());

        if (emp.getLicense().equals(LicenseType.ADMIN)){
            logAppt.setOnClickListener(v -> {
                Intent i = new Intent(MainEmployeeActivity.this, EmployeeLogServiceActivity.class);
                startActivity(i);
            });

            logAppt.setVisibility(View.VISIBLE);
            bookAppt.setVisibility(View.GONE);
            viewSchedule.setVisibility(View.GONE);
        }

        viewSchedule.setOnClickListener(v -> {
            Intent i = new Intent(MainEmployeeActivity.this, EmployeeViewScheduleActivity.class);
            startActivity(i);
        });

        bookAppt.setOnClickListener(v -> {
            Intent i = new Intent(MainEmployeeActivity.this, EmployeeBookServiceActivity.class);
            startActivity(i);
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
        Intent i = new Intent(MainEmployeeActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
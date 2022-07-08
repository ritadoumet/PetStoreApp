package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeViewScheduleActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout servicesList;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_view_schedule);

        username=findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        servicesList=findViewById(R.id.services_list);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());

        Employee emp = (Employee) User.getCurrentUser();
        for (Service s: emp.Services){
            LinearLayout newLayout = new LinearLayout(EmployeeViewScheduleActivity.this);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT ));
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setPadding(20,25,20,20);
            newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

            String serviceName = s.getType().toString();

            TextView serviceNameText = new TextView(EmployeeViewScheduleActivity.this);
            serviceNameText.setText(serviceName);
            serviceNameText.setTextSize(20);
            serviceNameText.setTextColor(getResources().getColor(R.color.brown));

            Booking booking = null;
            for (User u : User.Users){
                if (u.getUserType().equals(UserType.OWNER)){
                    for (Booking b: u.Bookings){
                        if (s.getServiceID()==b.getBookingID()){
                            booking = b; break;
                        }
                    }
                }
            }
            TextView petInfo = new TextView(EmployeeViewScheduleActivity.this);
            petInfo.setText("Date: "+booking.getBookedDay()+" - "+ booking.getBookedMonth()+" - "+ booking.getBookedYear()+", at "+ booking.getBookingHour()+" o'clock.\nPet ID: "+ booking.getPetID()+"\nOwner ID: "+ booking.getOwnerID());
            petInfo.setPadding(5, 5, 5, 15);
            petInfo.setTextColor(getResources().getColor(R.color.beige));

            newLayout.addView(serviceNameText);
            newLayout.addView(petInfo);

            servicesList.addView(newLayout);
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
        Intent i = new Intent(EmployeeViewScheduleActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
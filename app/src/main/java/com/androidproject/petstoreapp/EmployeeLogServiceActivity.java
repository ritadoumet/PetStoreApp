package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeLogServiceActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout bookingsLayout;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_log_service);

        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        bookingsLayout = findViewById(R.id.bookings_list);
        logout.setOnClickListener(v -> logoutDialog());

        for (User u : User.Users){
            for (Booking booking : u.Bookings){

                LinearLayout newLayout = new LinearLayout(EmployeeLogServiceActivity.this);
                newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                newLayout.setOrientation(LinearLayout.VERTICAL);
                newLayout.setPadding(20, 25, 20, 20);
                newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

                StringBuilder info = new StringBuilder("Booking Info: \n\n");
                info.append("Pet ID: ").append(booking.getPetID()).append("\n");
                info.append("Owner ID: ").append(booking.getOwnerID()).append("\n");
                info.append("Status: ").append(booking.getStatus().toString()).append("\n");

                int nbOfServices = 0;
                double paymentAmount = 0;
                for (Service s: u.Services){
                    if (booking.getBookingID()==s.getBookingID()){
                    info.append("\nService ").append(nbOfServices + 1).append(": ").append(s.getType().toString()).append("\n");
                    if (s.getEmpID()==-1){
                        info.append(" * not booked by any employee * \n");
                    }
                    else {
                        info.append("Employee ID: ").append(s.getEmpID()).append("\n");
                        paymentAmount+=s.getPrice();
                    }
                    nbOfServices++;}
                }
                String title =booking.getBookedDay() + " - " + (booking.getBookedMonth() + 1) + " - " + booking.getBookedYear()+ ", at " + booking.getBookedHour() + " o'clock, ID: " + booking.getBookingID();
               TextView bookingTitle = new TextView(EmployeeLogServiceActivity.this);
                bookingTitle.setText(title);
                bookingTitle.setTextSize(20);
                bookingTitle.setTextColor(getResources().getColor(R.color.brown));

                TextView apptInfo = new TextView(EmployeeLogServiceActivity.this);
                apptInfo.setText(info.toString());
                apptInfo.setPadding(5, 5, 5, 15);
                apptInfo.setTextColor(getResources().getColor(R.color.beige));

                Button logBookingBtn = new Button(EmployeeLogServiceActivity.this);
                logBookingBtn.setBackgroundColor(getResources().getColor(R.color.beige));
                logBookingBtn.setGravity(Gravity.CENTER_HORIZONTAL);
                logBookingBtn.setText("Mark as completed");
                logBookingBtn.setTextColor(getResources().getColor(R.color.white));

                double finalPaymentAmount = paymentAmount;
                logBookingBtn.setOnClickListener(v -> {
                    bookingsLayout.removeView(newLayout);
                    Intent i = new Intent(EmployeeLogServiceActivity.this, MakePaymentActivity.class);
                    i.putExtra("paymentAmount", finalPaymentAmount);
                    i.putExtra("userID", u.getUserID());
                    i.putExtra("purpose", "buyCart");
                    i.putExtra("bookingID", booking.getBookingID());
                    startActivity(i);
                });

                newLayout.addView(bookingTitle);
                newLayout.addView(apptInfo);
                if (booking.getStatus()!=BookingStatus.CANCELLED && booking.getStatus()!=BookingStatus.COMPLETED)
                  newLayout.addView(logBookingBtn);
                bookingsLayout.addView(newLayout);
            }
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
        Intent i = new Intent(EmployeeLogServiceActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
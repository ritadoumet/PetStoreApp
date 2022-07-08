package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewApptsActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout apptsList;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appts);

        username=findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        apptsList=findViewById(R.id.appts_list);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());


        Owner owner = (Owner)User.getCurrentUser();
        for (Booking b: owner.Bookings){
            if (b.getStatus().equals(BookingStatus.PENDING)){
            LinearLayout newLayout = new LinearLayout(ViewApptsActivity.this);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT ));
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setPadding(20,25,20,20);
            newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

            StringBuilder info = new StringBuilder("Your appointment info: \nServices: ");
            for (Service s: owner.Services){
                if (s.getBookingID()==b.getBookingID()) {
                    info.append(s.getType().toString()).append(" ");
                }
            }

            info.append("\nHour: ").append(b.getBookedHour()).append(" o'clock");

            String title = "";
            for (Pet p: owner.getPets()){
                if (b.getPetID()==p.getPetID()){
                    title+=p.getName();
                    break;
                }
            }
            title+="    "+ b.getBookedDay()+" - "+ (b.getBookedMonth()+1)+" - "+b.getBookedYear();
            TextView bookingTitle = new TextView(ViewApptsActivity.this);
            bookingTitle.setText(title);
            bookingTitle.setTextSize(20);
            bookingTitle.setTextColor(getResources().getColor(R.color.brown));

            TextView apptInfo = new TextView(ViewApptsActivity.this);
            apptInfo.setText(info.toString());
            apptInfo.setPadding(5, 5, 5, 15);
            apptInfo.setTextColor(getResources().getColor(R.color.beige));

            Button cancelApptBtn = new Button(ViewApptsActivity.this);
            cancelApptBtn.setBackgroundColor(getResources().getColor(R.color.beige));
            cancelApptBtn.setGravity(Gravity.CENTER_HORIZONTAL);
            cancelApptBtn.setText("Cancel Appointment");
            cancelApptBtn.setTextColor(getResources().getColor(R.color.white));
            cancelApptBtn.setOnClickListener(v -> {
                owner.cancelBooking(b.getBookingID());
                Toast.makeText(ViewApptsActivity.this, "Service booked! ", Toast.LENGTH_SHORT).show();
                apptsList.removeView(newLayout);
            });

            newLayout.addView(bookingTitle);
            newLayout.addView(apptInfo);
            newLayout.addView(cancelApptBtn);
            apptsList.addView(newLayout);}
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
        Intent i = new Intent(ViewApptsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
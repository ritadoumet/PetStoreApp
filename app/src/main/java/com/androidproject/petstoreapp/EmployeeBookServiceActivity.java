package com.androidproject.petstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeBookServiceActivity extends AppCompatActivity {
    TextView username;
    TextView logout;
    LinearLayout apptsList;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_book_service);

        username=findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        apptsList=findViewById(R.id.appts_list);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());

        Employee emp = (Employee) User.getCurrentUser();
        for (User u: User.Users){
            if (u.getUserType().equals(UserType.OWNER)) {
                User owner = u;

                for (Service service : owner.Services) {
                    if (service.getEmpID() == -1 && matchesSkills(service.getType(), emp)) {
                        Booking booking = null;
                        for (Booking b : owner.Bookings) {
                            if (b.getBookingID() == service.getBookingID()) {
                                booking = b;
                            }
                        }


                        LinearLayout newLayout = new LinearLayout(EmployeeBookServiceActivity.this);
                        newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        newLayout.setOrientation(LinearLayout.VERTICAL);
                        newLayout.setPadding(20, 25, 20, 20);
                        newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

                        String info = "Service Info: \n";

                        info += "Hour: " + booking.getBookedHour() + " o'clock\nOwnerID: " + booking.getOwnerID() + "\nBookingID: " + booking.getBookingID();

                        String title = "" + service.getType().toString();
                        title += "    " + booking.getBookedDay() + " - " + (booking.getBookedMonth() + 1) + " - " + booking.getBookedYear();
                        TextView bookingTitle = new TextView(EmployeeBookServiceActivity.this);
                        bookingTitle.setText(title);
                        bookingTitle.setTextSize(20);
                        bookingTitle.setTextColor(getResources().getColor(R.color.brown));

                        TextView apptInfo = new TextView(EmployeeBookServiceActivity.this);
                        apptInfo.setText(info);
                        apptInfo.setPadding(5, 5, 5, 15);
                        apptInfo.setTextColor(getResources().getColor(R.color.beige));

                        Button bookApptBtn = new Button(EmployeeBookServiceActivity.this);
                        bookApptBtn.setBackgroundColor(getResources().getColor(R.color.beige));
                        bookApptBtn.setGravity(Gravity.CENTER_HORIZONTAL);
                        bookApptBtn.setText("Book Appointment");
                        bookApptBtn.setTextColor(getResources().getColor(R.color.white));
                        bookApptBtn.setOnClickListener(v -> {
                            emp.insertNewService(service);
                            Toast.makeText(EmployeeBookServiceActivity.this, "Apointment booked!", Toast.LENGTH_SHORT).show();
                            apptsList.removeView(newLayout);
                        });

                        newLayout.addView(bookingTitle);
                        newLayout.addView(apptInfo);
                        newLayout.addView(bookApptBtn);
                        apptsList.addView(newLayout);

                }
            }

            }
        }
    }


    private boolean matchesSkills(ServiceType type, Employee emp){
        System.out.println(emp.getLicense().toString());
        if (emp.getLicense().equals(LicenseType.Anesthesiologist) || emp.getLicense().equals(LicenseType.Veterinary)){
            return true;
        }
        if (emp.getLicense().equals(LicenseType.Surgeon)){
            return type.equals(ServiceType.SURGERY);
        }
        if (emp.getLicense().equals(LicenseType.Groomer)){
            return type.equals(ServiceType.GROOMING) || type.equals(ServiceType.NAIL_TRIMMING) || type.equals(ServiceType.HAIRCUT);
        }
        if (emp.getLicense().equals(LicenseType.Walker)){
            return type.equals(ServiceType.PET_WALKING) || type.equals(ServiceType.PET_TRAINING);
        }

        return false;
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
        Intent i = new Intent(EmployeeBookServiceActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
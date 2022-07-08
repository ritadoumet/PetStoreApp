package com.androidproject.petstoreapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
public class BookApptActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    Spinner petSpinner;
    Spinner serviceSpinner1;
    Spinner serviceSpinner2;
    Spinner serviceSpinner3;
    AlertDialog logoutAlert;
    LinearLayout serviceLayout1;
    LinearLayout serviceLayout2;
    LinearLayout serviceLayout3;
    Button removeService2;
    Button removeService3;
    CalendarView calendar;
    TextView addServiceBtn;
    Button bookAptBtn;
    EditText hour;

    int aptHour=-1;
    int aptYear = -1;
    int aptMonth = -1;
    int aptDayOfMonth = -1;
    Pet petSelected = null;
    ServiceType[] servicesSelected;
    ArrayList<String> petsArray;
    ArrayList<String> servicesArray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appt);

        servicesSelected = new ServiceType[3];
        petSpinner = findViewById(R.id.pets_list);
        serviceSpinner1 = findViewById(R.id.service1_list);
        serviceSpinner2 = findViewById(R.id.service2_list);
        serviceSpinner3 = findViewById(R.id.service3_list);
        serviceLayout1 = findViewById(R.id.service1_layout);
        serviceLayout2 = findViewById(R.id.service2_layout);
        serviceLayout3 = findViewById(R.id.service3_layout);
        removeService2 = findViewById(R.id.remove_service2);
        removeService3 = findViewById(R.id.remove_service3);
        calendar = findViewById(R.id.booking_date);
        addServiceBtn = findViewById(R.id.add_service_btn);
        bookAptBtn = findViewById(R.id.book_appt_btn);
        hour = findViewById(R.id.hour);
        petsArray = new ArrayList<>();
        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);

        username.setText(User.getCurrentUser().getUsername());

        logout.setOnClickListener(v -> logoutDialog());
        Owner o = (Owner) User.getCurrentUser();


        for (Pet p : o.getPets()){
            petsArray.add(p.getName());
        }

        servicesArray = new ArrayList<>();

        for (ServiceType st: ServiceType.values()){
            servicesArray.add(st.name());
        }

        ArrayAdapter<String> petsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petsArray);
        ArrayAdapter<String> servicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicesArray);

        petsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        petSpinner.setAdapter(petsAdapter);
        serviceSpinner1.setAdapter(servicesAdapter);
        serviceSpinner2.setAdapter(servicesAdapter);
        serviceSpinner3.setAdapter(servicesAdapter);

        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                petSelected = o.getPets().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                petSelected = null;

            }
        });

        serviceSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicesSelected[0] = ServiceType.valueOf(servicesArray.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                servicesSelected[0] = null;
            }
        });
        serviceSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicesSelected[1] = ServiceType.valueOf(servicesArray.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                servicesSelected[1] = null;
            }
        });
        serviceSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicesSelected[2] = ServiceType.valueOf(servicesArray.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                servicesSelected[2] = null;
            }
        });

        addServiceBtn.setOnClickListener(v -> {
            if (serviceLayout2.getVisibility()==View.VISIBLE){
                serviceLayout3.setVisibility(View.VISIBLE);
                addServiceBtn.setEnabled(false);
                addServiceBtn.setTextColor(getResources().getColor(R.color.gray));
            }
            else{
                serviceLayout2.setVisibility(View.VISIBLE);
                if (serviceLayout3.getVisibility()==View.VISIBLE){
                    addServiceBtn.setEnabled(false);
                    addServiceBtn.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        removeService2.setOnClickListener(v -> {
            serviceLayout2.setVisibility(View.GONE);
            servicesSelected[1] = null;
            addServiceBtn.setEnabled(true);
        });
        removeService3.setOnClickListener(v -> {
            serviceLayout3.setVisibility(View.GONE);
            servicesSelected[2] = null;
            addServiceBtn.setEnabled(true);
        });

        calendar.setMinDate(Calendar.getInstance().getTimeInMillis());
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            aptYear = year;
            aptMonth = month;
            aptDayOfMonth = dayOfMonth;
        });


        bookAptBtn.setOnClickListener(v -> {
            if (validInput()){
                Booking b = new Booking(User.getCurrentUser().getUserID(), petSelected.getPetID(), aptDayOfMonth, aptMonth, aptYear, aptHour  );
                o.addBooking(b);

                for (int i = 0; i<3; i++){
                    if (servicesSelected[i]!=null){
                        Service s = new Service(o.getUserID(), petSelected.getPetID(), -1, b.getBookingID(), servicesSelected[i], ServiceType.valueOf(servicesSelected[i]));
                        o.addService(s);
                    }
                }

                Intent i = new Intent(BookApptActivity.this, ConfirmationActivity.class);
                i.putExtra("confirmation_msg", "Booking saved!\nDate: "+" - "+aptDayOfMonth+" - "+(aptMonth+1)+" - "+aptYear+"\n"+petSelected.getName()+" better be ready at "+aptHour+" O'clock!\n\nP.S: You will be charged for the services once they are marked as completed by one of our employees.");
                startActivity(i);
                finish();
            }
        });
    }

    private boolean validInput() {
        if (petSelected==null){
            Toast.makeText(this, "Please specify pet", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ( aptYear==-1 || aptDayOfMonth==-1 || aptMonth==-1){
            Toast.makeText(this, "Please specify date", Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            aptHour = Integer.parseInt(hour.getText().toString());
            if (aptHour<8 || aptHour>16){
                Toast.makeText(this, "Sorry we're not open at "+aptHour, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Please enter a valid hour between 8 and 16", Toast.LENGTH_SHORT).show();
            return false;
        }
        int nbOfServicesSelected = 0;
        for (int i = 0; i<3; i++){
            if (servicesSelected[i]!=null){
                nbOfServicesSelected++;
            }
        }
        if (nbOfServicesSelected==0){
            Toast.makeText(this, "Please specify the service(s) you want", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        Intent i = new Intent(BookApptActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
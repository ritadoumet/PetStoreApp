package com.androidproject.petstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class AddAPetActivity extends AppCompatActivity {

    TextView petName;
    CalendarView calendarView;
    Spinner speciesSpinner;
    AutoCompleteTextView furColor;
    Button addPetBtn;
    PetSpecies selectedSpecies = null;
    TextView username;
    TextView logout;
    AlertDialog logoutAlert;
    int yearOB, dayOB, monthOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apet);

        petName = findViewById(R.id.petName);
        calendarView=findViewById(R.id.calendarView);
        speciesSpinner = findViewById(R.id.spinnerSpecies);
        furColor = findViewById(R.id.furTextView);
        addPetBtn = findViewById(R.id.addPetBtn);
        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
        String furColors[]={"White", "Black", "Calico", "Orange", "Grey", "Brown"};
        String species[] = {PetSpecies.BIRD.toString(), PetSpecies.CAT.toString(), PetSpecies.COW.toString(), PetSpecies.DOG.toString(), PetSpecies.GOAT.toString(), PetSpecies.HORSE.toString(), PetSpecies.PIG.toString(), PetSpecies.SNAKE.toString()};

        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, species);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(speciesAdapter);

        ArrayAdapter<String> furAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, furColors);
        furColor.setAdapter(furAdapter);

        speciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(species[position].toLowerCase(Locale.ROOT)){
                    case "bird":selectedSpecies=PetSpecies.BIRD; break;
                    case "cat":selectedSpecies=PetSpecies.CAT; break;
                    case "cow":selectedSpecies=PetSpecies.COW; break;
                    case "dog":selectedSpecies=PetSpecies.DOG; break;
                    case "snake":selectedSpecies=PetSpecies.SNAKE; break;
                    case "goat":selectedSpecies=PetSpecies.GOAT; break;
                    case "horse":selectedSpecies=PetSpecies.HORSE; break;
                    case "pig":selectedSpecies=PetSpecies.PIG; break;
                    default: selectedSpecies=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    selectedSpecies=null;
            }
        });

        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInput()){
                    Owner owner = (Owner)User.getCurrentUser();
                    owner.insertNewPet(petName.getText().toString(), dayOB, monthOB, yearOB, selectedSpecies, furColor.getText().toString());
                    Toast.makeText(AddAPetActivity.this, "Pet "+petName.getText().toString()+" added succussfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                yearOB = year;
                monthOB= month;
                dayOB=dayOfMonth;
            }
        });

        furColor.setThreshold(1);
        calendarView.setMaxDate(Calendar.getInstance().getTimeInMillis());
        calendarView.setMinDate(1000*3600*24*365*12);
    }

    private void logoutDialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);

        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int which) {
                logout(); }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutAlert.cancel();
            }
        });

        logoutAlert= builder.create();
        logoutAlert.show();
    }

    private void logout() {
        User.setCurrentUser(null);
        Intent i = new Intent(AddAPetActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private boolean validInput() {
        String correctionToast = "Please enter valid input in: ";
        Owner owner = (Owner) User.getCurrentUser();
        boolean uniquePetName = owner.uniquePetName(petName.getText().toString());

        if (petName.getText().toString().equals("")){
            correctionToast+="pet name ";
        }
        if (furColor.getText().toString().equals("")){
            correctionToast+="fur color ";
        }
        if (selectedSpecies==null){
            correctionToast+="species ";
        }
        if (!uniquePetName){
            correctionToast+="\nYou already have a pet with this name.";
        }
        if (!correctionToast.equals("Please enter valid input in: ")){
            Toast.makeText(AddAPetActivity.this, correctionToast, Toast.LENGTH_LONG).show();
            return false;
        }
        else return true;
    }
}
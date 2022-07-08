package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewMyPetsActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout petsList;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_pets);

        username=findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        petsList=findViewById(R.id.pets_list);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());

        Owner owner = (Owner)User.getCurrentUser();
        for (Pet pet: owner.getPets()){
            LinearLayout newLayout = new LinearLayout(ViewMyPetsActivity.this);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT ));
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setPadding(20,25,20,20);
            newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

            String petName = pet.getName();
            String year=pet.getYear()+"";
            String month = pet.getMonth()+"";
            String day = pet.getDayOfMonth()+"";
            String color = pet.getFurColor();
            String species = pet.getSpecies().toString();

            TextView petNameText = new TextView(ViewMyPetsActivity.this);
            petNameText.setText(petName);
            petNameText.setTextSize(20);
            petNameText.setTextColor(getResources().getColor(R.color.brown));

            TextView petInfo = new TextView(ViewMyPetsActivity.this);
            petInfo.setText(color+" "+species+"\nBorn on "+day+" - "+month+" - "+year);
            petInfo.setPadding(5, 5, 5, 15);
            petInfo.setTextColor(getResources().getColor(R.color.beige));

            newLayout.addView(petNameText);
            newLayout.addView(petInfo);

            petsList.addView(newLayout);
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
        Intent i = new Intent(ViewMyPetsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
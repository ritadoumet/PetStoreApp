package com.androidproject.petstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseProductsActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout products_list;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);

        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        products_list = findViewById(R.id.products_list);

        username.setText(User.getCurrentUser().getUsername());
        logout.setOnClickListener(v -> logoutDialog());


        for (Product p: Product.products){
            LinearLayout newLayout = new LinearLayout(BrowseProductsActivity.this);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT ));
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setPadding(20,25,20,20);
            newLayout.setBackground(getResources().getDrawable(R.drawable.rectangle_back));

            String name = p.getName();
            String weight = p.getWeight()+"";
            String color = p.getColor();
            String desc = p.getDescription();
            String type = p.getType().toString();
            String species = p.getSpecies().toString();

            TextView nameView = new TextView(BrowseProductsActivity.this);
            nameView.setText(name);
            nameView.setTextSize(20);
            nameView.setTextColor(getResources().getColor(R.color.brown));
            if (p.getType().equals(ProductType.Food))
            nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_fastfood_16,0,0,0);
            if (p.getType().equals(ProductType.Accessory))
            nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_bubble_chart_16,0,0,0);
            if (p.getType().equals(ProductType.Medication))
            nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_medical_services_16,0,0,0);
            if (p.getType().equals(ProductType.Toy))
            nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_videogame_asset_16,0,0,0);

            nameView.setCompoundDrawablePadding(5);
            nameView.setPadding(5,5,5,5);

            TextView descView = new TextView(BrowseProductsActivity.this);
            descView.setText("Type: "+type+"\nWeight: "+weight+"\nDescription: "+desc+"\nSpecies: "+species+"\n"+color);
            descView.setTextSize(14);
            descView.setTextColor(getResources().getColor(R.color.beige));
            descView.setPadding(10,10,10,10);


            NumberPicker nb = new NumberPicker(BrowseProductsActivity.this);
            nb.setMinValue(1);
            nb.setMaxValue(20);
            nb.setBackgroundColor(getResources().getColor(R.color.transparentBeige));

            Button btnShow = new Button(this);
            btnShow.setText("Add to cart");
            btnShow.setTextColor(getResources().getColor(R.color.white));
            btnShow.setGravity(Gravity.CENTER_HORIZONTAL);
            btnShow.setBackgroundColor(getResources().getColor(R.color.brown));
            btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnShow.setOnClickListener(v -> {
                Owner o = (Owner) User.getCurrentUser();
                if (o.Cart.containsKey(p)  && o.Cart.get(p)==nb.getValue()){
                    Toast.makeText(BrowseProductsActivity.this, "Product" + name +" already in cart in the same quantity!", Toast.LENGTH_LONG).show();

                }
                else {
                    o.Cart.put(p, nb.getValue());
                    Toast.makeText(BrowseProductsActivity.this, "Product" + name + " added to cart", Toast.LENGTH_LONG).show();
                }
            });

            newLayout.addView(nameView);
            newLayout.addView(descView);
            newLayout.addView(nb);
            newLayout.addView(btnShow);
            products_list.addView(newLayout);
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
        Intent i = new Intent(BrowseProductsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
package com.androidproject.petstoreapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewMyCartActivity extends AppCompatActivity {

    TextView username;
    TextView logout;
    LinearLayout prod_list;
    AlertDialog logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_cart);
        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        prod_list = findViewById(R.id.products_list);

        username.setText(User.getCurrentUser().getUsername());

        logout.setOnClickListener(v -> logoutDialog());


        Owner o = (Owner) User.getCurrentUser();
        for (Product p:o.Cart.keySet()){
            LinearLayout newLayout = new LinearLayout(ViewMyCartActivity.this);
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

            Button removeBtn = new Button(ViewMyCartActivity.this);
            removeBtn.setBackgroundColor(getResources().getColor(R.color.beige));
            removeBtn.setGravity(Gravity.CENTER_HORIZONTAL);
            removeBtn.setText("Remove from cart");
            removeBtn.setTextColor(getResources().getColor(R.color.white));
            removeBtn.setOnClickListener(v -> {
                newLayout.setVisibility(View.GONE);
                o.removeFromCart(p);
            });
            TextView nameView = new TextView(ViewMyCartActivity.this);
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

            TextView descView = new TextView(ViewMyCartActivity.this);
            descView.setText("Type: "+type+"\nWeight: "+weight+"\nDescription: "+desc+"\nSpecies: "+species+"\n"+color);
            descView.setTextSize(14);
            descView.setTextColor(getResources().getColor(R.color.beige));
            descView.setPadding(10,10,10,10);

            TextView nb = new TextView(ViewMyCartActivity.this);
            nb.setText("Quantity: "+o.Cart.get(p));
            nb.setTextSize(14);
            nb.setTextColor(getResources().getColor(R.color.beige));
            nb.setPadding(10,10,10,10);

            newLayout.addView(nameView);
            newLayout.addView(descView);
            newLayout.addView(removeBtn);
            newLayout.addView(nb);
            prod_list.addView(newLayout);
        }

        Button purchaseCartBtn = new Button(ViewMyCartActivity.this);
        purchaseCartBtn.setBackgroundColor(getResources().getColor(R.color.beige));
        purchaseCartBtn.setGravity(Gravity.CENTER_HORIZONTAL);
        purchaseCartBtn.setText("Purchase Cart");
        purchaseCartBtn.setTextColor(getResources().getColor(R.color.white));
        purchaseCartBtn.setOnClickListener(v -> {
            Double paymentAmount =0.0;
            for (Product p : o.Cart.keySet()){
                paymentAmount+=p.getPrice()*o.Cart.get(p);
            }
            if (paymentAmount!=0){
                Intent i = new Intent(ViewMyCartActivity.this, MakePaymentActivity.class);
                i.putExtra("paymentAmount", paymentAmount);
                i.putExtra("userID", o.getUserID());
                i.putExtra("purpose", "buyCart");
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
        prod_list.addView(purchaseCartBtn);
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
        Intent i = new Intent(ViewMyCartActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
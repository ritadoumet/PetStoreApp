package com.androidproject.petstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView username;
    TextView password;
    Button loginbtn;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User.loadUsers();

        username = findViewById(R.id.username);
        password = findViewById((R.id.password));
        loginbtn = findViewById(R.id.loginbtn);
        signUp = findViewById(R.id.signup);


        loginbtn.setOnClickListener(v -> {

            User user =User.authenticateUser(username.getText().toString(), password.getText().toString());

            if (user!=null){
                signIn(user);
            }
            else{
                Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
            }
        });

        signUp.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }
    private void signIn(User user) {

        int UserID = user.getUserID();
        if (user.getUserType().equals(UserType.OWNER)){
            user = new Owner(user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhoneNumber());
            user.setUserID(UserID);
            Intent i = new Intent(LoginActivity.this, MainOwnerActivity.class);
           startActivity(i);
            finish();
        }
        else if (user.getUserType().equals(UserType.EMPLOYEE)){
            user = new Employee(user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getSsn(), user.getLicense());
            user.setUserID(UserID);
            Intent i = new Intent(LoginActivity.this, MainEmployeeActivity.class);
            startActivity(i);
            finish();
        }


        User.setCurrentUser(user);
    }
}
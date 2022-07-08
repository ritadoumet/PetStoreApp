package com.androidproject.petstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    UserType userType;
    TextView name;
    TextView email;
    TextView password;
    TextView phoneNumber;
    TextView userName;
    Button signUpButton;
    LinearLayout empLayout;
    Spinner userTypeSpinner;
    TextView ssn;
    LicenseType empType;
    Spinner empTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        userName = findViewById(R.id.username);
        signUpButton = findViewById(R.id.signUpButton);
        userTypeSpinner=findViewById(R.id.spinnerUserType);
        empLayout=findViewById(R.id.empLayout);
        empTypeSpinner = findViewById(R.id.spinnerEmpType);
        ssn = findViewById(R.id.ssn);

        ArrayList<String> userTypes = new ArrayList<>();

        userTypes.add(UserType.EMPLOYEE.toString());
        userTypes.add(UserType.OWNER.toString());

        ArrayAdapter<String> userTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);
        userTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userTypeSpinner.setAdapter(userTypesAdapter);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userTypes.get(position).equals("EMPLOYEE")){
                    userType = UserType.EMPLOYEE;
                    empLayout.setVisibility(View.VISIBLE);
                }
                else if (userTypes.get(position).equals("OWNER")){
                    userType=UserType.OWNER;
                    empLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userType = null;
                empLayout.setVisibility(View.GONE);
            }
        });

        ArrayList<String> empTypes = new ArrayList<>();

        empTypes.add(LicenseType.Walker.toString());
        empTypes.add((LicenseType.Anesthesiologist.toString()));
        empTypes.add(LicenseType.Groomer.toString());
        empTypes.add(LicenseType.Surgeon.toString());
        empTypes.add(LicenseType.Veterinary.toString());
        empTypes.add(LicenseType.ADMIN.toString());

        ArrayAdapter<String> empTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empTypes);
        empTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        empTypeSpinner.setAdapter(empTypesAdapter);
        empTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(empTypes.get(position).toLowerCase(Locale.ROOT)){
                    case "veterinarian": empType=LicenseType.Veterinary;
                    break;

                    case "anesthesiologist": empType=LicenseType.Anesthesiologist;
                    break;

                    case "walker": empType=LicenseType.Walker;
                    break;

                    case "groomer": empType=LicenseType.Groomer;
                    break;

                    case "surgeon": empType=LicenseType.Surgeon;
                    break;
                    case "admin":empType=LicenseType.ADMIN;break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    empType=null;
            }
        });

        signUpButton.setOnClickListener(v -> {
            try{
            if (correctData()){
                User newUser = saveUser();
                login(newUser);
            }}
            catch(Exception e){
                System.out.println(e.getMessage());
            }

        });

    }

    private void login(User user) {
        User.setCurrentUser(user);
        if (user.getUserType().equals(UserType.OWNER)){

            Intent i = new Intent(SignUpActivity.this, MainOwnerActivity.class);
            i.putExtra("username", user.getUsername());
            startActivity(i);
            finish();
        }
        else{
            Intent i = new Intent(SignUpActivity.this, MainEmployeeActivity.class);
            i.putExtra("username", user.getUsername());
            startActivity(i);
        }

    }

    private User saveUser() {
        User newUser = null;

        if(userType.equals(UserType.EMPLOYEE)){
            int ssnInt = Integer.parseInt(ssn.getText().toString());
            newUser = new Employee(userName.getText().toString(), password.getText().toString(), name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString(), ssnInt, empType);
        }
        else if (userType.equals(UserType.OWNER)){
            newUser = new Owner(userName.getText().toString(), password.getText().toString(), name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString());
        }
        User.addNewUser(newUser);

        return newUser;
    }

    private boolean correctData() {
        String correctionToast = "Please enter correct data for: ";
        boolean uniqueUserName = User.uniqueUsername(userName.getText().toString());
        boolean uniqueEmail = User.uniqueEmail(email.getText().toString());

        if (userName.getText().toString().trim().equals("")){userName.setText("");
            correctionToast+="username ";
        }
        if (name.getText().toString().trim().equals("")){name.setText("");
            correctionToast+="name ";
        }
        if (!validPass(password.getText().toString())){
            correctionToast+="password ";
            password.setText("");
        }
        if (phoneNumber.getText().toString().equals("")){
            correctionToast+="phoneNumber ";
            phoneNumber.setText("");
        }
        int ssnInt = -1;
        if (userType.equals(UserType.EMPLOYEE))
        try{
            ssnInt = Integer.parseInt(ssn.getText().toString());

        }
        catch(Exception e){
            correctionToast+="ssn ";
            ssn.setText("");
        }
        if (userType.equals(UserType.EMPLOYEE) && (ssn.getText().toString().equals("")|| ssnInt<1)){
            correctionToast+="ssn ";
            ssn.setText("");
        }

        if (userType.equals(UserType.EMPLOYEE) && empType==null){
            correctionToast+="employeeType";
        }
        if (email.getText().toString().equals("") || !email.getText().toString().contains("@") || (!email.getText().toString().contains(".com") && !email.getText().toString().contains(".net") && !email.getText().toString().contains(".org"))){
            correctionToast+="email ";
            email.setText("");
        }
        if (!uniqueUserName){
            correctionToast+="\n Username already taken";
            userName.setText("");
        }
        if (!uniqueEmail){
            correctionToast+="\n Email already taken";
            email.setText("");
        }
        if (!correctionToast.equals("Please enter correct data for: ")){
            Toast.makeText(SignUpActivity.this, correctionToast, Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validPass(String pass) {
        return pass.length() >= 4 && !pass.trim().equals("");
    }


}
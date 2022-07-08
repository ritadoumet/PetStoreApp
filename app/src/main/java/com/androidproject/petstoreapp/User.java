package com.androidproject.petstoreapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static User currentUser = null;
    private String name;
    private String email;
    private String phoneNumber;
    private String username;
    private int ssn;
    private LicenseType license;
    private String password;
    private int userID;
    private UserType userType;
    public static ArrayList<User> Users = new ArrayList<>();

    @Exclude
    public ArrayList<Pet> Pets;
    @Exclude
    public ArrayList<Booking> Bookings;
    @Exclude
    public ArrayList<Service> Services;
    @Exclude
    public Map<Product, Integer> Cart;

    private static int UserID=0;

    public User(String username, String password, String name, String email, String phoneNumber, UserType userType) {
        this.username = username;
        this.password = password;
        this.name=name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        userID = UserID++;
        Pets = new ArrayList<>();
        Cart= new HashMap<>() ;
        Bookings = new ArrayList<>();
        Services = new ArrayList<Service>();
    }

    public User() {

    }

    public User(String username, String password, String name, String email, String phoneNumber, UserType employee, int ssn, LicenseType license) {
        this.ssn=ssn;
        this.license=license;
        this.username = username;
        this.password = password;
        this.name=name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = employee;
        userID = UserID++;
        Pets = new ArrayList<>();
        Cart= new HashMap<>() ;
        Bookings = new ArrayList<>();
        Services = new ArrayList<>();
    }

    public static boolean uniqueUsername(String username) {
        for (User user:Users){
            if (user.getUsername().equals(username)){
                return false;
            }
        }
        return true;
    }

    public static boolean uniqueEmail(String email) {
        for (User u : Users){
            if (u.getEmail().equals(email))
                return false;
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public static void addNewUser(User user){
        Users.add(user);
        //TODO: insert to database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(User.class.getSimpleName());
        ref.child(user.getUserID()+"").setValue(user);
    }

    public static User authenticateUser(String username, String password) {
        for (User user : User.Users) {
            if (user.username.equals(username) && user.password.equals(password)){
                return user;
            }
        }
        System.out.println("User: "+Users.size());
        return null;
    }

    public static void loadUsers(){

        UserID=1;
        Users = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(User.class.getSimpleName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxUserID = User.UserID;
                for (DataSnapshot sn : snapshot.getChildren()){
                    User u = sn.getValue(User.class);
                    u.Services=new ArrayList<>();
                    u.Bookings = new ArrayList<>();
                    u.Cart= new HashMap<>() ;
                    u.Pets = new ArrayList<>();
                    User.Users.add(u);
                    if (u.getUserID()>maxUserID)
                     maxUserID = u.getUserID();
                }
                User.UserID=maxUserID+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public LicenseType getLicense() {
        return license;
    }

    public void setLicense(LicenseType license) {
        this.license = license;
    }


}

package com.androidproject.petstoreapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Payment {
    private PaymentType type;
    private double value;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int UserID;
    private static int PaymentID = 0;
    private int paymentID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Payment(PaymentType type, double value, int userID) {
        this.type = type;
        this.value = value;
        this.day = LocalDateTime.now().getDayOfMonth();
        this.month =  LocalDateTime.now().getMonth().getValue();
        this.year =  LocalDateTime.now().getYear();
        this.hour =  LocalDateTime.now().getHour();
        UserID = userID;
        this.paymentID = PaymentID++;
    }


    public Payment(){}
    public static void insertPayment(Payment payment) {
        //TODO DATABASE
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref =db.getReference(Payment.class.getSimpleName());
        ref.child(payment.getPaymentID()+"").setValue(payment);
    }

    public static void setPAYMENTID(int paymentID){PaymentID=paymentID;}
    public static int getPAYMENTID(){return PaymentID;}

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getPaymentID() {
        return this.paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
}

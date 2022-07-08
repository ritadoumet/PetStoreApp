package com.androidproject.petstoreapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Service {

    private static int ServiceID = 0;
    private int serviceID;
    private int ownerID;
    private int petID;
    private int empID;
    private int bookingID;
    private String completionTime;
    private ServiceType type;
    private int price;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Service(int ownerID, int petID, int empID, int bookingID, ServiceType type, int price) {
        this.ownerID = ownerID;
        this.petID = petID;
        this.empID = empID;
        this.bookingID = bookingID;
        this.type = type;
        this.price = price;
        this.serviceID = ServiceID++;
        this.completionTime=" ";
    }

    public Service(){}
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static void setSERVICEID(int serviceID){ServiceID=serviceID;}
    public static int getSERVICEID(){return ServiceID;}
}

package com.androidproject.petstoreapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

public class Booking {
    private static int BookingID = 0;
    private int bookingID;
    private int ownerID;
    private int petID;
    private int bookingDay, bookedDay;
    private int bookingMonth, bookedMonth;
    private int bookingYear, bookedYear;
    private int bookingHour, bookedHour;
    private BookingStatus status;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Booking(int ownerID, int petID, int bookedDay, int bookedMonth, int bookedYear, int bookedHour) {
        this.ownerID = ownerID;
        this.petID = petID;
        this.bookedDay = bookedDay;
        this.bookedMonth = bookedMonth;
        this.bookedYear = bookedYear;
        this.bookedHour = bookedHour;
        this.bookingDay = LocalDateTime.now().getDayOfMonth();
        this.bookingHour = LocalDateTime.now().getHour();
        this.bookingYear = LocalDateTime.now().getYear();
        this.bookingMonth=LocalDateTime.now().getMonth().getValue();
        this.status = BookingStatus.PENDING;
        this.bookingID = BookingID++;
    }

    public Booking(){}
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public static int getBOOKINGID(){return BookingID;}
    public static void setBOOKINGID(int bookingID){BookingID=bookingID;}

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

    public int getBookingDay() {
        return bookingDay;
    }

    public void setBookingDay(int bookingDay) {
        this.bookingDay = bookingDay;
    }

    public int getBookedDay() {
        return bookedDay;
    }

    public void setBookedDay(int bookedDay) {
        this.bookedDay = bookedDay;
    }

    public int getBookingMonth() {
        return bookingMonth;
    }

    public void setBookingMonth(int bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    public int getBookedMonth() {
        return bookedMonth;
    }

    public void setBookedMonth(int bookedMonth) {
        this.bookedMonth = bookedMonth;
    }

    public int getBookingYear() {
        return bookingYear;
    }

    public void setBookingYear(int bookingYear) {
        this.bookingYear = bookingYear;
    }

    public int getBookedYear() {
        return bookedYear;
    }

    public void setBookedYear(int bookedYear) {
        this.bookedYear = bookedYear;
    }

    public int getBookingHour() {
        return bookingHour;
    }

    public void setBookingHour(int bookingHour) {
        this.bookingHour = bookingHour;
    }

    public int getBookedHour() {
        return bookedHour;
    }

    public void setBookedHour(int bookedHour) {
        this.bookedHour = bookedHour;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void completeBooking(int bookingID) {
        //TODO
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Booking.class.getSimpleName());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", BookingStatus.CANCELLED);
        ref.child(bookingID+"").updateChildren(hashMap);

        for (User u:User.Users){
            for (Booking b: u.Bookings){
                if (b.getBookingID()==bookingID){
                    b.setStatus(BookingStatus.COMPLETED);
                    return;
                }
            }
        }

        DatabaseReference servRef = db.getReference(Service.class.getSimpleName());
        for (User u : User.Users){
            for (Service s: u.Services){
                if (s.getBookingID()==bookingID){
                    s.setCompletionTime(LocalDateTime.now().toString());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("completionTime", s.getCompletionTime());
                    servRef.child(s.getServiceID()+"").updateChildren(map);
                }
            }
        }

    }
}

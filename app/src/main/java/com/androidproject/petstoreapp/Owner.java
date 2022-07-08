package com.androidproject.petstoreapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Owner extends User{

    public Owner(String username, String password, String name, String email, String phoneNumber) {
        super(username, password, name, email, phoneNumber, UserType.OWNER);

    }

    public void addBooking(Booking b) {
        //TODO IMPLEMENT IN DATABASE
        this.Bookings.add(b);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Booking.class.getSimpleName());
        ref.child(b.getBookingID()+"").setValue(b);
    }

    public ArrayList<Pet> getPets() {
        return Pets;
    }

    public void setPets(ArrayList<Pet> pets) {
        Pets = pets;
    }

    public boolean uniquePetName(String name){
        for (Pet pet:Pets){
            if (pet.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    public void insertNewPet(String name, int dayOfMonth, int month, int year, PetSpecies species, String furColor) {
        //TODO implement database if ever developed

        Pet pet = new Pet(name, dayOfMonth, month, year, species, this.getUserID(), furColor);
        this.Pets.add(pet);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Pet.class.getSimpleName());
        ref.child(pet.getPetID()+"").setValue(pet);
    }

    public void removeFromCart(Product p) {
        Cart.remove(p);
        //TODO database
    }

    public void addService(Service s) {
        this.Services.add(s);
        //TODO database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Service.class.getSimpleName());
        ref.child(s.getServiceID()+"").setValue(s);
    }


    public void cancelBooking(int bookingID) {
        //TODO
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Booking.class.getSimpleName());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", BookingStatus.CANCELLED);
        ref.child(bookingID+"").updateChildren(hashMap);

        for (Booking b : Bookings){
            if (b.getBookingID()==bookingID){
                b.setStatus(BookingStatus.CANCELLED);
                return;
            }
        }
    }

    public void loadData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        Pet.setPET_ID(0);
        Service.setSERVICEID(0);
        Booking.setBOOKINGID(0);
        Product.setPRODUCTID(0);
        Payment.setPAYMENTID(0);

        DatabaseReference petRef = db.getReference(Pet.class.getSimpleName());
        DatabaseReference servRef = db.getReference(Service.class.getSimpleName());
        DatabaseReference bookingRef = db.getReference(Booking.class.getSimpleName());
        DatabaseReference productRef = db.getReference(Product.class.getSimpleName());
        DatabaseReference paymentRef = db.getReference(Payment.class.getSimpleName());

        petRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxPetID = Pet.getPET_ID();
                for (DataSnapshot sn : snapshot.getChildren()){
                    Pet p = sn.getValue(Pet.class);
                    if (p.getPetID()>maxPetID)
                        maxPetID=p.getPetID();
                    if (p.getOwnerID()==getUserID()) {
                        Pets.add(p);
                    }
                }
                Pet.setPET_ID(maxPetID+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        servRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxServID = Service.getSERVICEID();
                for (DataSnapshot sn : snapshot.getChildren()){
                    Service s = sn.getValue(Service.class);
                    if (s.getServiceID()>maxServID)
                        maxServID=s.getServiceID();
                    if (s.getOwnerID()==getUserID())
                        Services.add(s);
                }
                Service.setSERVICEID(maxServID+1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxProdID = Product.getPRODUCTID();
                for (DataSnapshot sn: snapshot.getChildren()){
                    Product p = sn.getValue(Product.class);
                    if (p.getProductID()>maxProdID)
                        maxProdID=p.getProductID();
                    Product.products.add(p);
                }
                Product.setPRODUCTID(maxProdID+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxBookingID = Booking.getBOOKINGID();
                for (DataSnapshot sn: snapshot.getChildren()){
                    Booking b = sn.getValue(Booking.class);
                    if (b.getBookingID()>maxBookingID)
                        maxBookingID=b.getBookingID();
                    if (b.getOwnerID()==getUserID())
                      Bookings.add(b);
                }
                Booking.setBOOKINGID(maxBookingID+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxPaymentID = Payment.getPAYMENTID();
                for (DataSnapshot sn: snapshot.getChildren()){
                    Payment p = sn.getValue(Payment.class);
                    if (p.getPaymentID()>maxPaymentID)
                        maxPaymentID=p.getPaymentID();

                }
                Payment.setPAYMENTID(maxPaymentID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

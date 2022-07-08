package com.androidproject.petstoreapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Employee extends User{


    ArrayList<Service> Services;
    public Employee(String username, String password, String name, String email, String phoneNumber, int ssn, LicenseType license) {
        super(username, password, name, email, phoneNumber, UserType.EMPLOYEE, ssn, license);
        this.Services= new ArrayList<>();
    }

    public void insertNewService(Service service) {
        //TODO DATABASE: UPDATE SERVICE: SET EMP ID TO THIS.GETUSERID()

        service.setEmpID(this.getUserID());
        Services.add(service);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("serviceID", service.getServiceID());
        hashMap.put("ownerID", service.getOwnerID());
        hashMap.put("petID", service.getPetID());
        hashMap.put("empID", this.getUserID());
        hashMap.put("bookingID", service.getBookingID());
        hashMap.put("completionTime", service.getCompletionTime());
        hashMap.put("type", service.getType());
        hashMap.put("price", service.getPrice());

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(Service.class.getSimpleName());
        ref.child(service.getServiceID()+"").updateChildren(hashMap);
    }

    public void loadData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference petRef = db.getReference(Pet.class.getSimpleName());
        DatabaseReference servRef = db.getReference(Service.class.getSimpleName());
        DatabaseReference bookingRef = db.getReference(Booking.class.getSimpleName());
        DatabaseReference paymentRef = db.getReference(Payment.class.getSimpleName());

        petRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxPetID = Pet.getPET_ID();
                for (DataSnapshot sn : snapshot.getChildren()){
                    Pet p = sn.getValue(Pet.class);
                    if (p.getPetID()>maxPetID)
                        maxPetID=p.getPetID();
                    for (User u : User.Users){
                        if (u.getUserType().equals(UserType.OWNER) && u.getUserID()==p.getOwnerID()) {
                            u.Pets.add(p);
                            break;
                        }
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
                    if (s.getEmpID()==getUserID())
                        Services.add(s);
                    for (User u : User.Users){
                        if (u.getUserType().equals(UserType.OWNER) && u.getUserID()==s.getOwnerID()){
                            u.Services.add(s);
                            break;
                        }
                    }
                }
                Service.setSERVICEID(maxServID+1);
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
                   for (User u : User.Users){
                       if (u.getUserType().equals(UserType.OWNER) && b.getOwnerID()==u.getUserID()){
                           u.Bookings.add(b);
                       }
                   }
                }
                Booking.setBOOKINGID(maxBookingID+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

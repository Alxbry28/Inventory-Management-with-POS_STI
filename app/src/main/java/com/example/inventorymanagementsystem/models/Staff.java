package com.example.inventorymanagementsystem.models;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Staff {

    public static final String TABLE = "tblStaff";
    private String id, userId, storeId;
    private String firstname, middlename, lastname, address, mobile_number, position;
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public Staff() {
        realtimeFirebaseDB = new RealtimeFirebaseDB();
        dbRef = realtimeFirebaseDB.StaffTable();
    }

    public void Create(final TransactionStatusListener transactionStatus) {
        this.setId(dbRef.push().getKey());
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void Delete(final TransactionStatusListener transactionStatus) {
        dbRef.child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void GetByUserId(final StaffModelListener staffModelListener) {
        Query query = dbRef.orderByChild("userId").equalTo(this.getUserId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Staff staffExist = null;
                    for (DataSnapshot staffSnapshot : snapshot.getChildren()) {
                        staffExist = staffSnapshot.getValue(Staff.class);
                    }
                    staffModelListener.retrieveStaff(staffExist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetAll(final StaffModelListener staffModelListener) {
        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Staff> staffArrayList = new ArrayList<>();
                    for (DataSnapshot staffSnapShot : snapshot.getChildren()) {
                        Staff staff = staffSnapShot.getValue(Staff.class);
                        staffArrayList.add(staff);
                    }
                    staffModelListener.getStaffList(staffArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetById(final StaffModelListener staffModelListener) {
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Staff staffExist = snapshot.getValue(Staff.class);
                    staffModelListener.retrieveStaff(staffExist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Update(final TransactionStatusListener transactionStatus) {
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void GetBusinessOwner(final StaffModelListener staffModelListener) {
        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Staff> staffList = new ArrayList<>();
                for (DataSnapshot staffSnapShot : snapshot.getChildren()) {
                    Staff staff = staffSnapShot.getValue(Staff.class);
                    staffList.add(staff);
                }

                Staff staffOwner = staffList.stream().filter(staff -> staff.getPosition().equals("Business Owner")).findFirst().orElse(null);

                staffModelListener.retrieveStaff(staffOwner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getFullname() {
        return getFirstname() + " " + getLastname();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

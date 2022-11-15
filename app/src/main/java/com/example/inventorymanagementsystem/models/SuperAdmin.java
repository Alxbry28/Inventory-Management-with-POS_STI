package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.google.firebase.database.DatabaseReference;

// Mobile App Owner
public class SuperAdmin extends Staff {

    public static final String TABLE = "tblSuperAdmin";
    private RealtimeFirebaseDB realtimeFirebaseDB;

    public SuperAdmin() {
        realtimeFirebaseDB = new RealtimeFirebaseDB();
        super.setPosition("Super Administrator");
    }

    @Override
    public void Create(TransactionStatusListener transactionStatus) {
        DatabaseReference dbRef = realtimeFirebaseDB.SuperAdminTable();
        this.setId(dbRef.push().getKey());
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }
}

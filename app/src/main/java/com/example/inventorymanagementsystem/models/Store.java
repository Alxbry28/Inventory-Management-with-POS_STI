package com.example.inventorymanagementsystem.models;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.StoreModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Store {

    public static final String TABLE = "tblStore";
    private String id,userId;
    private String name,owner;
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public Store(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        dbRef = realtimeFirebaseDB.StoreTable();
    }

    public void Create(final TransactionStatusListener transactionStatus){
        this.setId(dbRef.push().getKey());
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }
    public void GetByUserId(final StoreModelListener storeModelListener){
        Query query = dbRef.orderByChild("userId").equalTo(this.getUserId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Store storeExist = null;
                    for (DataSnapshot storeSnapshot : snapshot.getChildren()){
                        storeExist = storeSnapshot.getValue(Store.class);
                    }
                    storeModelListener.retrieveStore(storeExist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetById(final StoreModelListener storeModelListener){
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Store storeExist = snapshot.getValue(Store.class);
                    storeModelListener.retrieveStore(storeExist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

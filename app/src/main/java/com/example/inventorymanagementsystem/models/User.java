package com.example.inventorymanagementsystem.models;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class User {

    public static final String TABLE = "tblUsers";

    private String id;
    private String key;
    private String storeId;
    private String busName, email, password;
    private int userType;
    private boolean isLogin;
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public User(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        dbRef = realtimeFirebaseDB.UserTable();
    }

    public void Create(final TransactionStatusListener transactionStatus){
//        this.setPassword(null);
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
                transactionStatus.checkStatus(task.isSuccessful());
        });
    }
    public void Delete(final TransactionStatusListener transactionStatus){
        dbRef.child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }
    public void GetById(final UserModelListener userModelListener){
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User userExist = snapshot.getValue(User.class);
                    userModelListener.retrieveUser(userExist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusName() {
        return busName;
    }

    public String getEmail() {
        return email;
    }



}

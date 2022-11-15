package com.example.inventorymanagementsystem.models;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Product {

    private int quantity;
    private String id, userId, storeId;
    private String name, category;
    private double price, cost;
    private boolean isDeleted;

    public static final String TABLE = "tblProducts";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public Product(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.ProductsTable();
    }

    public void Create(final TransactionStatusListener transactionStatus){
        this.setId(dbRef.push().getKey());
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void Delete(final TransactionStatusListener transactionStatus){
        dbRef.child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void GetById(final ProductModelListener productModelListener){
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product productExist = snapshot.getValue(Product.class);
                    productModelListener.retrieveProduct(productExist);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetAll(final ProductModelListener productModelListener){
        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Product> productArrayList = new ArrayList<>();
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    Product product = productSnapShot.getValue(Product.class);
                    productArrayList.add(product);
                }
                productModelListener.getProductList(productArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

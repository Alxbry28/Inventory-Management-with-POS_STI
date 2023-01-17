package com.example.inventorymanagementsystem.models;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Product {

    private int quantity, restock;
    private String id, userId, storeId;
    private String name, category;
    private String imageUrl;
    private double price, cost;
    private String created_at, updated_at;
    private String created_time, updated_time;
    private boolean isDeleted;

    public static final String TABLE = "tblProducts";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;
    private String dateTime;

    public Product(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.ProductsTable();
        dateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public int stockStatus(){
//        int tempRestock = getRestock() + 1;
//        int tempQty = getQuantity() - 1;
        if(getQuantity() <= 5){
            return 0;
        }
        else if(getRestock() >= getQuantity()){
            return 1;
        }

        return 2;
    }

    public void Create(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setId(dbRef.push().getKey());
        this.setCreated_at(date);
        this.setUpdated_at(date);
        this.setCreated_time(time);
        this.setUpdated_time(time);
        dbRef.child(this.getStoreId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void Update(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setUpdated_at(date);
        this.setUpdated_time(time);
        dbRef.child(this.getStoreId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    public void Delete(TransactionStatusListener transactionStatus) {
        dbRef.child(this.getStoreId()).child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }


    public double GetComputedTotalPrice(){
        return quantity * price;
    }

    public void GetById(final ProductModelListener productModelListener){
        Query query = dbRef.child(this.getStoreId()).child(this.getId());
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
        Query query = dbRef.child(this.getStoreId());
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

    public void GetAllByDateRange(String startDate, String endDate, final ProductModelListener productModelListener) {
        Query query = dbRef.child(this.getStoreId()).orderByChild("updated_at").startAt(startDate).endAt(endDate);
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


    public void Search(String search,final ProductModelListener productModelListener){
        Query query = dbRef.orderByChild("storeId").startAt(search).endAt(search + "\uf0ff");
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

    public static int findIndexById(ArrayList<Product> productList, String productId){
        for(int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == productId) {
                return i;
            }
        }
        return -1;
    }

    //Getters and Settes
    public double getCost() {
        return cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getRestock() {
        return restock;
    }

    public void setRestock(int restock) {
        this.restock = restock;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
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

        double roundOffPrice = (double) Math.round(price * 100) / 100;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

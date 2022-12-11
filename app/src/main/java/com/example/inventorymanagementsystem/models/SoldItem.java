package com.example.inventorymanagementsystem.models;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.IModelRepository;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SoldItem implements IModelRepository<SoldItem> {

    private int quantity;
    private String id, userId,  productId, salesId, storeId;
    private String name, category;
    private double productPrice, totalPrice;
    private String created_at, updated_at;
    private String created_time, updated_time;
    private boolean isDeleted;

    public static final String TABLE = "tblSoldItems";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public SoldItem(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.SoldItemTable();
    }

    @Override
    public void Create(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setId(dbRef.push().getKey());
        this.setCreated_at(date);
        this.setUpdated_at(date);
        this.setCreated_time(time);
        this.setUpdated_time(time);
        dbRef.child(this.getStoreId()).child(this.getSalesId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Update(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setUpdated_at(date);
        this.setUpdated_time(time);
        dbRef.child(this.getStoreId()).child(this.getSalesId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Delete(TransactionStatusListener transactionStatus) {
        dbRef.child(this.getStoreId()).child(this.getSalesId()).child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void GetById(IEntityModelListener<SoldItem> entityModelListener) {
        Query query = dbRef.child(this.getStoreId()).child(this.getSalesId()).child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    SoldItem soldItemExist = snapshot.getValue(SoldItem.class);
                    entityModelListener.retrieve(soldItemExist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void GetAll(IEntityModelListener<SoldItem> entityModelListener) {
        Query query = dbRef.child(this.getStoreId()).child(this.getSalesId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SoldItem> soldItemsList = new ArrayList<>();
                for (DataSnapshot soldItemSnapshot : snapshot.getChildren()) {
                    SoldItem soldItemExist = soldItemSnapshot.getValue(SoldItem.class);
                    soldItemsList.add(soldItemExist);
                }
                entityModelListener.getList(soldItemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public double GetComputedSubtotal(){
        return quantity * productPrice;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

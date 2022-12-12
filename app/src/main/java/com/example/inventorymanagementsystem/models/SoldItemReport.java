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

public class SoldItemReport implements IModelRepository<SoldItemReport> {

    private int quantity;
    private String id, userId,  productId, salesId, storeId;
    private String name, category;
    private double productPrice, totalPrice;
    private String created_at, updated_at;
    private String created_time, updated_time;
    private boolean isDeleted;
    private String receiptNo;

    public static final String TABLE = "tblSoldItemReport";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public SoldItemReport(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.SoldItemReportTable();
    }

    public SoldItemReport(String name, String category, int quantity, double productPrice, String created_at, String created_time) {
        this.quantity = quantity;
        this.name = name;
        this.category = category;
        this.productPrice = productPrice;
        this.created_at = created_at;
        this.created_time = created_time;
    }

    public void setSoldItemToReport(SoldItem soldItem) {
        this.quantity = soldItem.getQuantity();
        this.id = soldItem.getId();
        this.userId = soldItem.getUserId();
        this.productId = soldItem.getProductId();
        this.salesId = soldItem.getSalesId();
        this.storeId = soldItem.getStoreId();
        this.name = soldItem.getName();
        this.category = soldItem.getCategory();
        this.productPrice = soldItem.getProductPrice();
        this.totalPrice = soldItem.GetComputedSubtotal();
        this.created_at = soldItem.getCreated_at();
        this.updated_at = soldItem.getUpdated_at();
        this.created_time = soldItem.getCreated_time();
        this.updated_time = soldItem.getUpdated_time();
    }

    @Override
    public void Create(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setId(this.getId());
        this.setCreated_at(date);
        this.setUpdated_at(date);
        this.setCreated_time(time);
        this.setUpdated_time(time);
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Update(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setUpdated_at(date);
        this.setUpdated_time(time);
//        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
        dbRef.child(this.getStoreId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Delete(TransactionStatusListener transactionStatus) {
        dbRef.child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void GetById(IEntityModelListener<SoldItemReport> entityModelListener) {
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    SoldItemReport soldItemExist = snapshot.getValue(SoldItemReport.class);
                    entityModelListener.retrieve(soldItemExist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void GetAllBySalesId(IEntityModelListener<SoldItemReport> entityModelListener) {
        Query query = dbRef.orderByChild("salesId").equalTo(this.getSalesId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SoldItemReport> soldItemsList = new ArrayList<>();
                for (DataSnapshot soldItemSnapshot : snapshot.getChildren()) {
                    SoldItemReport soldItemExist = soldItemSnapshot.getValue(SoldItemReport.class);
                    soldItemsList.add(soldItemExist);
                }

                entityModelListener.getList(soldItemsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void GetAll(IEntityModelListener<SoldItemReport> entityModelListener) {
//        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId());
        Query query = dbRef.child(this.getStoreId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SoldItemReport> soldItemsList = new ArrayList<>();
                for (DataSnapshot soldItemSnapshot : snapshot.getChildren()) {
                    SoldItemReport soldItemExist = soldItemSnapshot.getValue(SoldItemReport.class);
                    soldItemsList.add(soldItemExist);
                }
                entityModelListener.getList(soldItemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetAllByDateRange(String startDate, String endDate, IEntityModelListener<SoldItemReport> entityModelListener) {
        Query query = dbRef.child(this.getStoreId()).orderByChild("created_at").startAt(startDate).endAt(endDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SoldItemReport> soldItemsList = new ArrayList<>();
                for (DataSnapshot soldItemSnapshot : snapshot.getChildren()) {
                    SoldItemReport soldItemExist = soldItemSnapshot.getValue(SoldItemReport.class);
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

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}

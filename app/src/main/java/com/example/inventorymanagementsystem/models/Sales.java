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

public class Sales implements IModelRepository<Sales> {

    private String id, product_id, storeId, userId;
    private int quantity;
    private double total_price;
    private String status, createdAt;
    private String receiptNo;
    private String customerName;
    private double price, cost;
    private double amountPayable, amountReceived, amountChange;
    private String created_at, updated_at;
    private boolean isDeleted;

    public static final String TABLE = "tblSales";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;
    private String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(new Date());

    public Sales(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.SalesTable();
    }

    @Override
    public void Create(TransactionStatusListener transactionStatus) {
        this.setId(dbRef.push().getKey());
        this.setCreated_at(dateTime);
        this.setUpdated_at(dateTime);
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Update(TransactionStatusListener transactionStatus) {
        this.setUpdated_at(new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(new Date()));
        dbRef.child(this.getId()).setValue(this).addOnCompleteListener(task -> {
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
    public void GetById(IEntityModelListener<Sales> entityModelListener) {
        Query query = dbRef.child(this.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Sales salesExist = snapshot.getValue(Sales.class);
                    entityModelListener.retrieve(salesExist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String GeneratedInvoiceNumber(){
        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId()).limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Sales salesExist = snapshot.getValue(Sales.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return "";
    }

    @Override
    public void GetAll(IEntityModelListener<Sales> entityModelListener) {
        Query query = dbRef.orderByChild("storeId").equalTo(this.getStoreId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Sales> soldItemsList = new ArrayList<>();
                for (DataSnapshot soldItemSnapshot : snapshot.getChildren()) {
                    Sales soldItemExist = soldItemSnapshot.getValue(Sales.class);
                    soldItemsList.add(soldItemExist);
                }
                entityModelListener.getList(soldItemsList);
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public double getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(double amountChange) {
        this.amountChange = amountChange;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package com.example.inventorymanagementsystem.models;

import android.os.Environment;
import android.text.TextUtils;

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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Sales implements IModelRepository<Sales> {

    //Properties
    private String id, storeId, userId;
    private int quantity;
    private String receiptNo;
    private String customerName;
    private double amountPayable, amountReceived, amountChange;
    private String created_at, updated_at;
    private String created_time, updated_time;
    private boolean isDeleted;

    public static final String TABLE = "tblSales";
    public static final String FILENAME = "sales_generated_report.xls";
    public static final File FILE_PATH = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Sales.FILENAME);

    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public Sales(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.SalesTable();
    }

    // Unit Testing Purposes
    public Sales(int quantity, double amountPayable, double amountReceived, double amountChange, String created_at){
        this.quantity = quantity;
        this.amountPayable = amountPayable;
        this.amountReceived = amountReceived;
        this.amountChange = amountChange;
        this.created_at = created_at;
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
        dbRef.child(this.getStoreId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Update(TransactionStatusListener transactionStatus) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        this.setUpdated_at(date);
        this.setUpdated_time(time);
        dbRef.child(this.getStoreId()).child(this.getId()).setValue(this).addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void Delete(TransactionStatusListener transactionStatus) {
        dbRef.child(this.getStoreId()).child(this.getId()).removeValue().addOnCompleteListener(task -> {
            transactionStatus.checkStatus(task.isSuccessful());
        });
    }

    @Override
    public void GetById(IEntityModelListener<Sales> entityModelListener) {
        Query query = dbRef.child(this.getStoreId()).child(this.getId());
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
        String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("sshhmmSSS", Locale.getDefault()).format(new Date());
        return "IN"+date+"-"+time;
    }

    @Override
    public void GetAll(IEntityModelListener<Sales> entityModelListener) {
        Query query = dbRef.child(this.getStoreId());
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

    public void GetAllByDateRange(String startDate, String endDate, IEntityModelListener<Sales> entityModelListener) {
        Query query = dbRef.child(this.getStoreId()).orderByChild("created_at").startAt(startDate).endAt(endDate);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
}

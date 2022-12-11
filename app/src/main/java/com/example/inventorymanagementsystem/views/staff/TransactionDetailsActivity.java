package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.SoldItemRCVAdapter;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;
import com.example.inventorymanagementsystem.models.Staff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionDetailsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private String salesId, receiptNo;
    private Sales sales;
    private Staff staff;
    private SoldItem soldItem;
    private ArrayList<SoldItem> soldItemArrayList;
    private TextView tvReceiptNo, tvAmountPayable, tvCashReceived, tvChangeAmount, tvStaffTendered, tvDate,tvTotalItems;
    private Button btnBack;
    private RecyclerView rcTransactionDetails;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);
        rcTransactionDetails = findViewById(R.id.rcTransactionDetails);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(TransactionDetailsActivity.this, TransactionsForm.class));
            finish();
        });

        if (getIntent().hasExtra("transaction.id")) {
            staff = new Staff();
            staff.setStoreId(storeId);

            sales = new Sales();
            soldItem = new SoldItem();

            salesId = getIntent().getStringExtra("transaction.id");
            receiptNo = getIntent().getStringExtra("transaction.receiptNo");

            tvReceiptNo = findViewById(R.id.tvReceiptNo);
            tvTotalItems = findViewById(R.id.tvTotalItems);
            tvAmountPayable = findViewById(R.id.tvAmountPayable);
            tvCashReceived = findViewById(R.id.tvCashReceived);
            tvChangeAmount = findViewById(R.id.tvChangeAmount);
            tvStaffTendered = findViewById(R.id.tvStaffTendered);
            tvDate = findViewById(R.id.tvDate);

            tvReceiptNo.setText(receiptNo);
            sales.setStoreId(storeId);
            sales.setId(salesId);
            sales.GetById(new IEntityModelListener<Sales>() {
                @Override
                public void retrieve(Sales m) {
                    sales = m;
                    tvAmountPayable.setText(MoneyLibrary.toTwoDecimalPlaces(sales.getAmountPayable()));
                    tvCashReceived.setText(MoneyLibrary.toTwoDecimalPlaces(sales.getAmountReceived()));
                    tvChangeAmount.setText(MoneyLibrary.toTwoDecimalPlaces(sales.getAmountChange()));
                    tvTotalItems.setText("Items: " + sales.getQuantity());

                    LocalDate date = LocalDate.parse(sales.getCreated_at());
                    tvDate.setText("Date: " + dateTimeDateShort.format(date) + " " + sales.getCreated_time());

                    staff.setUserId(sales.getUserId());
                    staff.GetByUserId(new StaffModelListener() {
                        @Override
                        public void retrieveStaff(Staff staff) {
                            tvStaffTendered.setText("Tendered By: " + staff.getFullname());
                        }

                        @Override
                        public void getStaffList(ArrayList<Staff> staffList) {

                        }
                    });

                }

                @Override
                public void getList(ArrayList<Sales> salesArrayList) {

                }
            });

            soldItem.setStoreId(storeId);
            soldItem.setSalesId(salesId);
            soldItem.GetAll(new IEntityModelListener<SoldItem>() {
                @Override
                public void retrieve(SoldItem m) {

                }

                @Override
                public void getList(ArrayList<SoldItem> soldItems) {
                    initRCTransactionItems(soldItems);
                }
            });

        }

    }

    private void initRCTransactionItems(ArrayList<SoldItem> soldItemArrayList) {
        SoldItemRCVAdapter soldItemRCVAdapter = new SoldItemRCVAdapter();
        soldItemRCVAdapter.setContext(TransactionDetailsActivity.this);
        soldItemRCVAdapter.setActivity(TransactionDetailsActivity.this);
        soldItemRCVAdapter.setSoldItemArrayList(soldItemArrayList);
        rcTransactionDetails.setAdapter(soldItemRCVAdapter);
        rcTransactionDetails.setHasFixedSize(true);
        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(TransactionDetailsActivity.this);
        rcTransactionDetails.setLayoutManager(rcvLayoutManager);
        rcTransactionDetails.setItemAnimator(new DefaultItemAnimator());
    }

}
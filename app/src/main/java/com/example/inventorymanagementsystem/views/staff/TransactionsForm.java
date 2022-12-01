package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventorymanagementsystem.adapters.TransactionRCVAdapter;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
public class TransactionsForm extends AppCompatActivity {

    private Button btnBack;

    private ArrayList<Transaction> transactionsArrayList;
    private RecyclerView rcTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_form);

        rcTransaction = findViewById(R.id.rcTransaction);

        transactionsArrayList = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setCreatedAt("Customer 1 - Oct 26, 2022");
        transaction1.setTotal_price(100);
        transaction1.setQuantity(10);
        transaction1.setId(1);

        Transaction transaction2 = new Transaction();
        transaction2.setCreatedAt("Customer 2 - Oct 27, 2022");
        transaction2.setTotal_price(200);
        transaction2.setQuantity(10);
        transaction2.setId(3);

        Transaction transaction3 = new Transaction();
        transaction3.setCreatedAt("Customer 3 - Oct 28, 2022");
        transaction3.setTotal_price(150);
        transaction3.setQuantity(9);
        transaction3.setId(2);

        Transaction transaction4 = new Transaction();
        transaction4.setCreatedAt("Customer 4 - Nov 03, 2022");
        transaction4.setTotal_price(100);
        transaction4.setQuantity(10);
        transaction4.setId(4);

        transactionsArrayList.add(transaction1);
        transactionsArrayList.add(transaction2);
        transactionsArrayList.add(transaction3);
        transactionsArrayList.add(transaction4);

        TransactionRCVAdapter transactionRCVAdapter = new TransactionRCVAdapter();
        transactionRCVAdapter.setTransactionsList(transactionsArrayList);

        rcTransaction.setAdapter(transactionRCVAdapter);

        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(TransactionsForm.this);
        rcTransaction.setLayoutManager(rcvLayoutManager);
        rcTransaction.setItemAnimator(new DefaultItemAnimator());

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
                startActivity(new Intent(TransactionsForm.this,HomeActivity.class));
        });
    }

}
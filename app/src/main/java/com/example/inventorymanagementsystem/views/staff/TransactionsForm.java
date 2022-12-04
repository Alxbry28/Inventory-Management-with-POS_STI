package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.TransactionRCVAdapter;
import com.example.inventorymanagementsystem.dialogs.DurationChoiceDialog;
import com.example.inventorymanagementsystem.dialogs.MDCDatePickerDialog;
import com.example.inventorymanagementsystem.interfaces.IDurationChoiceDialogListener;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class TransactionsForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private Button btnBack, btnSelectDuration, btnStartDate, btnEndDate;
    private ArrayList<Transaction> transactionsArrayList;
    private RecyclerView rcTransaction;
    private Sales sales;
    private DurationChoiceDialog durationChoiceDialog;
    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_form);
        initDialog();
        MaterialDatePicker<Long> materialDatePicker = MDCDatePickerDialog.openDatePicker();
        MaterialDatePicker<Long> materialDatePickerEndDate = MDCDatePickerDialog.endDatePicker();

        MaterialDatePicker materialDateRangePicker = MDCDatePickerDialog.openDateRangePicker();

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        rcTransaction = findViewById(R.id.rcTransaction);
        sales = new Sales();
        sales.setStoreId(storeId);
        sales.GetAll(new IEntityModelListener<Sales>() {
            @Override
            public void retrieve(Sales m) {

            }

            @Override
            public void getList(ArrayList<Sales> salesArrayList) {
//                Toast.makeText(TransactionsForm.this, "size " + salesArrayList.size(), Toast.LENGTH_SHORT).show();
                initRCTransaction(salesArrayList);
            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
                startActivity(new Intent(TransactionsForm.this,HomeActivity.class));
        });

        btnSelectDuration = findViewById(R.id.btnSelectDuration);


        btnSelectDuration.setOnClickListener(v->{
            Toast.makeText(TransactionsForm.this, btnSelectDuration.getText().toString(), Toast.LENGTH_SHORT).show();
            durationChoiceDialog.show(getSupportFragmentManager(), "DURATION_CHOICE_DIALOG");
            durationChoiceDialog.setChosenDuration(btnSelectDuration.getText().toString());
            durationChoiceDialog.setiDurationChoiceDialogListener(new IDurationChoiceDialogListener() {
                @Override
                public void setChosenDuration(String chosenDuration) {
                    btnSelectDuration.setText(chosenDuration);

                }
            });

        });

        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(v->{
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(selection ->  {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatShortDate = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate  = format.format(calendar.getTime());
            String formattedShortDate  = formatShortDate.format(calendar.getTime());
            btnStartDate.setText(formattedShortDate);
            }
        );

        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(v->{
            materialDatePickerEndDate.show(getSupportFragmentManager(), "DATE_PICKER_END_DATE");
        });

        materialDatePickerEndDate.addOnPositiveButtonClickListener(selection ->  {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatShortDate = new SimpleDateFormat("dd MMM yyyy");
                    String formattedDate  = format.format(calendar.getTime());
                    String formattedShortDate  = formatShortDate.format(calendar.getTime());
                    btnStartDate.setText(formattedShortDate);
                }
        );


    }

    private void initDialog(){
        durationChoiceDialog = new DurationChoiceDialog();
        durationChoiceDialog.setContext(TransactionsForm.this);
    }

    private void initRCTransaction(ArrayList<Sales> salesArrayList){
        TransactionRCVAdapter transactionRCVAdapter = new TransactionRCVAdapter();
        transactionRCVAdapter.setSalesList(salesArrayList);

        rcTransaction.setAdapter(transactionRCVAdapter);

        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(TransactionsForm.this);
        rcTransaction.setLayoutManager(rcvLayoutManager);
        rcTransaction.setItemAnimator(new DefaultItemAnimator());

    }

}
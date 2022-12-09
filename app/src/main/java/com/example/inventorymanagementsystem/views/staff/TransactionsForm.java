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
import com.example.inventorymanagementsystem.enums.DateDuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;

public class TransactionsForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private Button btnBack, btnSelectDuration, btnStartDate, btnEndDate;
    private ArrayList<Transaction> transactionsArrayList;
    private RecyclerView rcTransaction;
    private Sales sales;
    private DurationChoiceDialog durationChoiceDialog;
    private String startDate, endDate, startDateShort, endDateShort;
    private String dateToday, dateTodayShorted;
    private MaterialDatePicker<Long> materialDatePickerStartDate, materialDatePickerEndDate;
    private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormatterShort = new SimpleDateFormat("dd MMM yyyy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_form);

        currentDate = LocalDate.now();
        initDialog();

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);

        rcTransaction = findViewById(R.id.rcTransaction);
        sales = new Sales();
        sales.setStoreId(storeId);
        selectedDuration(dateTimeFormatter.format(currentDate));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(TransactionsForm.this, HomeActivity.class));
        });

        btnSelectDuration = findViewById(R.id.btnSelectDuration);
        btnSelectDuration.setOnClickListener(v -> {
            durationChoiceDialog.show(getSupportFragmentManager(), "DURATION_CHOICE_DIALOG");
            durationChoiceDialog.setChosenDuration(btnSelectDuration.getText().toString());
            durationChoiceDialog.setiDurationChoiceDialogListener(new IDurationChoiceDialogListener() {
                @Override
                public void setChosenDuration(String chosenDuration) {
                    btnSelectDuration.setText(chosenDuration);
                    selectedDuration(chosenDuration);
                }
            });

        });

        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setText(dateTodayShorted);
        btnStartDate.setOnClickListener(v -> {
            materialDatePickerStartDate.show(getSupportFragmentManager(), "DATE_PICKER");
        });

        materialDatePickerStartDate.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                    calendar.setTimeInMillis(selection);
                    String formattedDate = simpleDateFormatter.format(calendar.getTime());
                    String formattedShortDate = simpleDateFormatterShort.format(calendar.getTime());
                    btnStartDate.setText(formattedShortDate);
                }
        );

        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setText(dateTodayShorted);
        btnEndDate.setOnClickListener(v -> {
            materialDatePickerEndDate.show(getSupportFragmentManager(), "DATE_PICKER_END_DATE");
        });

        materialDatePickerEndDate.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                    calendar.setTimeInMillis(selection);
                    endDate = simpleDateFormatter.format(calendar.getTime());
                    String formattedShortDate = simpleDateFormatterShort.format(calendar.getTime());
                    btnEndDate.setText(formattedShortDate);
                }
        );

    }

    private void selectedDuration(String dateDuration) {
        initRCTransaction(new ArrayList<Sales>());
        LocalDate firstDaylastMonth;
        switch (dateDuration) {
            case "Today":
                startDate = dateTimeFormatter.format(currentDate);
                endDate = startDate;
                startDateShort = dateTimeDateShort.format(currentDate);
                btnStartDate.setText(startDateShort);
                btnEndDate.setText(startDateShort);
                break;

            case "Last Day":

                LocalDate lastDay = currentDate.minusDays(1);
                startDate = dateTimeFormatter.format(lastDay);
                endDate = startDate;
                startDateShort =  dateTimeDateShort.format(lastDay);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(startDateShort);

                break;

            case "This Week":

                Toast.makeText(this, dateDuration + " ko na", Toast.LENGTH_SHORT).show();
                break;

            case "Last Week":
                Toast.makeText(this, dateDuration + " ko na", Toast.LENGTH_SHORT).show();
                break;

            case "Last Month":
                LocalDate lastMonth = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                firstDaylastMonth = lastMonth.with(TemporalAdjusters.firstDayOfMonth());

                startDate = dateTimeFormatter.format(firstDaylastMonth);
                endDate = dateTimeFormatter.format(lastMonth);

                Toast.makeText(this, "Last startDate: " + startDate + "; endDate: " + endDate, Toast.LENGTH_SHORT).show();

                startDateShort = dateTimeDateShort.format(firstDaylastMonth);
                endDateShort = dateTimeDateShort.format(lastMonth);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "This Month":
                firstDaylastMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

                startDate = dateTimeFormatter.format(firstDaylastMonth);
                endDate = dateTimeFormatter.format(currentDate);

                Toast.makeText(this, "This startDate: " + startDate + "; endDate: " + endDate, Toast.LENGTH_SHORT).show();

                startDateShort = dateTimeDateShort.format(firstDaylastMonth);
                endDateShort = dateTimeDateShort.format(currentDate);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "All":
                sales.GetAll(new IEntityModelListener<Sales>() {
                    @Override
                    public void retrieve(Sales m) {

                    }

                    @Override
                    public void getList(ArrayList<Sales> salesArrayList) {
                        initRCTransaction(salesArrayList);
                    }
                });
                return;

            case "Single Day":
                Toast.makeText(this, dateDuration + " ko na", Toast.LENGTH_SHORT).show();
                return;

            case "Date Range":
                Toast.makeText(this, dateDuration + " ko na", Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(this, "Default ko na", Toast.LENGTH_SHORT).show();
                break;
        }

        sales.GetAllByDateRange(startDate, endDate, new IEntityModelListener<Sales>() {
            @Override
            public void retrieve(Sales m) {

            }

            @Override
            public void getList(ArrayList<Sales> salesArrayList) {
                initRCTransaction(salesArrayList);
            }
        });

    }

    private void initDialog() {
        durationChoiceDialog = new DurationChoiceDialog();
        durationChoiceDialog.setContext(TransactionsForm.this);
        dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateTodayShorted = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        materialDatePickerStartDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerEndDate = MDCDatePickerDialog.endDatePicker(dateToday);
        Toast.makeText(this, "dateToday" + dateToday, Toast.LENGTH_SHORT).show();
    }

    private void initRCTransaction(ArrayList<Sales> salesArrayList) {
        TransactionRCVAdapter transactionRCVAdapter = new TransactionRCVAdapter();
        transactionRCVAdapter.setSalesList(salesArrayList);

        rcTransaction.setAdapter(transactionRCVAdapter);
        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(TransactionsForm.this);
        rcTransaction.setLayoutManager(rcvLayoutManager);
        rcTransaction.setItemAnimator(new DefaultItemAnimator());

    }

}
package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import java.time.DayOfWeek;
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
    private TextView tvTotal;
    private RecyclerView rcTransaction;
    private Sales sales;
    private ArrayList<Sales> tempSalesArrayList;
    private DurationChoiceDialog durationChoiceDialog;
    private String startDate, endDate, startDateShort, endDateShort;
    private String dateToday, dateTodayShorted;
    private MaterialDatePicker<Long> materialDatePickerStartDate, materialDatePickerEndDate;
    private MaterialDatePicker<Long>  materialDatePickerRangeStartDate, materialDatePickerRangeEndDate;
    private MaterialDatePicker<Long> materialDatePickerSingleDate, materialDatePickerRangeDate;
    private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormatterShort = new SimpleDateFormat("dd MMM yyyy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private LocalDate currentDate;
    private FragmentManager fragmentManager;
    private TextView tvEmptyTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_form);
        tempSalesArrayList = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        currentDate = LocalDate.now();
        startDate = dateTimeFormatter.format(currentDate);
        endDate = dateTimeFormatter.format(currentDate);

        initDialog();
        tvEmptyTransaction = findViewById(R.id.tvEmptyTransaction);
        tvTotal = findViewById(R.id.tvTotal);

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);

        rcTransaction = findViewById(R.id.rcTransaction);
        sales = new Sales();
        sales.setStoreId(storeId);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(TransactionsForm.this, HomeActivity.class));
            finish();
        });

        btnSelectDuration = findViewById(R.id.btnSelectDuration);
        selectedDuration(btnSelectDuration.getText().toString());
        btnSelectDuration.setOnClickListener(v -> {
            durationChoiceDialog.show(fragmentManager, "DURATION_CHOICE_DIALOG");
            durationChoiceDialog.setChosenDuration(btnSelectDuration.getText().toString());
            durationChoiceDialog.setiDurationChoiceDialogListener((String chosenDuration) -> {
                    btnSelectDuration.setText(chosenDuration);
                    selectedDuration(chosenDuration);
            });
        });

        setFirstEventDateRange();
        setSecondEventDateRange();

        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setText(dateTodayShorted);

        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setText(dateTodayShorted);

        firstEventListener();
    }

    private void removeFragmentDatePicker(){
        Fragment oldStartDate = fragmentManager.findFragmentByTag("DATE_PICKER_RANGE_START_DATE");
        Fragment oldEndDate = fragmentManager.findFragmentByTag("DATE_PICKER_RANGE_END_DATE");
        if (oldStartDate != null) {
            fragmentManager.beginTransaction().remove(oldStartDate).commit();
            if (oldStartDate.isAdded()) {
                fragmentManager.beginTransaction().remove(oldStartDate).commit();
            }
        }

        if (oldEndDate != null) {
            fragmentManager.beginTransaction().remove(oldEndDate).commit();
            if(oldEndDate.isAdded()){
                fragmentManager.beginTransaction().remove(oldEndDate).commit();
            }
        }

    }

    private void selectedDuration(String dateDuration) {
        initRCTransaction(new ArrayList<Sales>());
        LocalDate firstDay, lastDay;
        switch (dateDuration) {
            case "Today":
                startDate = dateTimeFormatter.format(currentDate);
                endDate = startDate;
                startDateShort = dateTimeDateShort.format(currentDate);
                btnStartDate.setText(startDateShort);
                btnEndDate.setText(startDateShort);
                break;

            case "Last Day":

                lastDay = currentDate.minusDays(1);
                startDate = dateTimeFormatter.format(lastDay);
                endDate = startDate;
                startDateShort = dateTimeDateShort.format(lastDay);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(startDateShort);
                break;

            case "This Week":
                firstDay = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(currentDate);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(currentDate);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "Last Week":
                firstDay = currentDate.minusWeeks(1).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
                lastDay = firstDay.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(lastDay);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(lastDay);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "Last Month":
                lastDay = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                firstDay = lastDay.with(TemporalAdjusters.firstDayOfMonth());

                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(lastDay);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(lastDay);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "This Month":
                firstDay = currentDate.with(TemporalAdjusters.firstDayOfMonth());

                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(currentDate);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(currentDate);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "Last Year":
                lastDay = currentDate.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                firstDay = lastDay.with(TemporalAdjusters.firstDayOfYear());

                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(lastDay);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(lastDay);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "This Year":
                firstDay = currentDate.with(TemporalAdjusters.firstDayOfYear());
                startDate = dateTimeFormatter.format(firstDay);
                endDate = dateTimeFormatter.format(currentDate);

                startDateShort = dateTimeDateShort.format(firstDay);
                endDateShort = dateTimeDateShort.format(currentDate);

                btnStartDate.setText(startDateShort);
                btnEndDate.setText(endDateShort);
                break;

            case "All":
                setFirstEventDateRange();
                sales.GetAll(new IEntityModelListener<Sales>() {
                    @Override
                    public void retrieve(Sales m) {

                    }

                    @Override
                    public void getList(ArrayList<Sales> salesArrayList) {
                        if(salesArrayList != null || (!salesArrayList.isEmpty())){
                            tempSalesArrayList = salesArrayList;

                            if(salesArrayList.size() > 0){
                                LocalDate start = LocalDate.parse(salesArrayList.get(0).getCreated_at());
                                LocalDate end = LocalDate.parse(salesArrayList.get(salesArrayList.size() - 1).getCreated_at());
                                startDateShort = dateTimeDateShort.format(start);
                                endDateShort = dateTimeDateShort.format(end);
                                btnStartDate.setText(startDateShort);
                                btnEndDate.setText(endDateShort);
                            }

                            initRCTransaction(salesArrayList);
                        }
                    }
                });
                return;

            case "Single Day":
                materialDatePickerSingleDate.show(fragmentManager, "DATE_PICKER_SINGLE_DATE");
                materialDatePickerSingleDate.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                    calendar.setTimeInMillis(selection);
                    String formattedDate = simpleDateFormatter.format(calendar.getTime());
                    String formattedShortDate = simpleDateFormatterShort.format(calendar.getTime());
                    sales.GetAllByDateRange(formattedDate, formattedDate, new IEntityModelListener<Sales>() {
                        @Override
                        public void retrieve(Sales m) {

                        }

                        @Override
                        public void getList(ArrayList<Sales> salesArrayList) {
                            tempSalesArrayList = salesArrayList;
                            initRCTransaction(salesArrayList);
                        }
                    });
                    btnStartDate.setText(formattedShortDate);
                    btnEndDate.setText(formattedShortDate);
                });
                return;

            case "Date Range":
                secondEventListener();
                btnStartDate.callOnClick();
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
                tempSalesArrayList = salesArrayList;
                initRCTransaction(salesArrayList);
            }
        });

    }

    private void setFirstEventDateRange(){
        materialDatePickerStartDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            String formattedDate = simpleDateFormatter.format(calendar.getTime());
            String formattedShortDate = simpleDateFormatterShort.format(calendar.getTime());
            startDate = formattedDate;
            startDateShort = formattedShortDate;
            sales.GetAllByDateRange(startDate, endDate, new IEntityModelListener<Sales>() {
                @Override
                public void retrieve(Sales m) {

                }

                @Override
                public void getList(ArrayList<Sales> salesArrayList) {
                    initRCTransaction(salesArrayList);
                }
            });
            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");
        });

        materialDatePickerEndDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            endDate = simpleDateFormatter.format(calendar.getTime());
            endDateShort = simpleDateFormatterShort.format(calendar.getTime());
            sales.GetAllByDateRange(startDate, endDate, new IEntityModelListener<Sales>() {
                @Override
                public void retrieve(Sales m) {

                }

                @Override
                public void getList(ArrayList<Sales> salesArrayList) {
                    initRCTransaction(salesArrayList);
                }
            });
            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");
        });

    }

    private void firstEventListener(){
        View.OnClickListener buttonRangeClickListener = (View v) ->{
            switch (v.getId()){
                case R.id.btnStartDate:
                    materialDatePickerStartDate.show(fragmentManager, "DATE_PICKER_START_DATE");
                    break;
                case R.id.btnEndDate:
                    materialDatePickerEndDate.show(fragmentManager, "DATE_PICKER_END_DATE");
                    break;
            }
        };

        btnStartDate.setOnClickListener(buttonRangeClickListener);
        btnEndDate.setOnClickListener(buttonRangeClickListener);
    }

    private void secondEventListener(){
        View.OnClickListener buttonRangeClickListener = (View v) ->{
            switch (v.getId()){
                case R.id.btnStartDate:
                    materialDatePickerRangeStartDate.show(fragmentManager, "DATE_PICKER_RANGE_START_DATE");
                    return;
                case R.id.btnEndDate:
                    materialDatePickerRangeEndDate.show(fragmentManager, "DATE_PICKER_RANGE_END_DATE");
                    firstEventListener();
                    return;
            }
        };
        btnStartDate.setOnClickListener(buttonRangeClickListener);
        btnEndDate.setOnClickListener(buttonRangeClickListener);
    }

    private void setSecondEventDateRange(){
        materialDatePickerRangeStartDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            String formattedDate = simpleDateFormatter.format(calendar.getTime());
            String formattedShortDate = simpleDateFormatterShort.format(calendar.getTime());
            startDate = formattedDate;
            startDateShort = formattedShortDate;
            btnSelectDuration.setText("Date Range");
            removeFragmentDatePicker();
            btnEndDate.callOnClick();
        });

        materialDatePickerRangeEndDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            endDate = simpleDateFormatter.format(calendar.getTime());
            endDateShort = simpleDateFormatterShort.format(calendar.getTime());
            sales.GetAllByDateRange(startDate, endDate, new IEntityModelListener<Sales>() {
                @Override
                public void retrieve(Sales m) {

                }

                @Override
                public void getList(ArrayList<Sales> salesArrayList) {
                    initRCTransaction(salesArrayList);
                }
            });

            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");

        });

    }

    private void initDialog() {
        durationChoiceDialog = new DurationChoiceDialog();
        durationChoiceDialog.setContext(TransactionsForm.this);
        dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateTodayShorted = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        materialDatePickerStartDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerSingleDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerRangeStartDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerEndDate = MDCDatePickerDialog.endDatePicker(dateToday);
        materialDatePickerRangeEndDate = MDCDatePickerDialog.endDatePicker(dateToday);
    }

    private void initRCTransaction(ArrayList<Sales> salesArrayList) {
        TransactionRCVAdapter transactionRCVAdapter = new TransactionRCVAdapter();
        transactionRCVAdapter.setContext(TransactionsForm.this);
        transactionRCVAdapter.setActivity(TransactionsForm.this);
        transactionRCVAdapter.setSalesList(salesArrayList);
        rcTransaction.setAdapter(transactionRCVAdapter);
        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(TransactionsForm.this);
        rcTransaction.setLayoutManager(rcvLayoutManager);
        rcTransaction.setItemAnimator(new DefaultItemAnimator());
        int visibility = (salesArrayList.size() <= 0) ? View.VISIBLE : View.GONE;
        tvEmptyTransaction.setVisibility(visibility);
        tvTotal.setText("Total Transaction: " + salesArrayList.size());
    }

}
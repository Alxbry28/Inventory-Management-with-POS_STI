package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.dialogs.DurationChoiceDialog;
import com.example.inventorymanagementsystem.dialogs.MDCDatePickerDialog;
import com.example.inventorymanagementsystem.interfaces.IDurationChoiceDialogListener;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.example.inventorymanagementsystem.models.Sales;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.services.MailerService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class SalesForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private Button btnBack, btnSelectDuration, btnStartDate, btnEndDate;
    private Button btnSendMail, btnGenerateReport;
    private PieChart pChartProducts;
    private BarChart bChartSales;
    private ArrayList<Sales> salesArrayList;
    private DurationChoiceDialog durationChoiceDialog;
    private String startDate, endDate, startDateShort, endDateShort;
    private String dateToday, dateTodayShorted;
    private MaterialDatePicker<Long> materialDatePickerStartDate, materialDatePickerEndDate;
    private MaterialDatePicker<Long> materialDatePickerRangeStartDate, materialDatePickerRangeEndDate;
    private MaterialDatePicker<Long> materialDatePickerSingleDate, materialDatePickerRangeDate;
    private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormatterShort = new SimpleDateFormat("dd MMM yyyy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private LocalDate currentDate;
    private FragmentManager fragmentManager;
    private TextView tvEmptyTransaction;
    private Sales sales;
    private ArrayList<Sales> tempSalesArrayList;
    private MailerService mailerService;
    private User userOwner;
    private Staff staff;
    private String receiverEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_form);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        tempSalesArrayList = new ArrayList<>();
        mailerService = new MailerService();
        fragmentManager = getSupportFragmentManager();
        currentDate = LocalDate.now();
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        sales = new Sales();
        staff= new Staff();
        userOwner = new User();
        initDialog();

        btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(SalesForm.this, HomeActivity.class));
        });

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);
        staff.setStoreId(storeId);
        sales.setStoreId(storeId);

        staff.GetBusinessOwner(new StaffModelListener() {
            @Override
            public void retrieveStaff(Staff staff) {
                userOwner.setId(staff.getUserId());
                userOwner.GetById(new UserModelListener() {
                    @Override
                    public void retrieveUser(User user) {
                        receiverEmail = user.getEmail();
                    }
                });
            }

            @Override
            public void getStaffList(ArrayList<Staff> staffList) {

            }
        });


        btnSelectDuration = findViewById(R.id.btnSelectDuration);
        selectedDuration(btnSelectDuration.getText().toString());
        btnSelectDuration.setOnClickListener(v -> {
            durationChoiceDialog.show(fragmentManager, "DURATION_CHOICE_DIALOG");
            durationChoiceDialog.setChosenDuration(btnSelectDuration.getText().toString());
            durationChoiceDialog.setiDurationChoiceDialogListener(new IDurationChoiceDialogListener() {
                @Override
                public void setChosenDuration(String chosenDuration) {
                    btnSelectDuration.setText(chosenDuration);
                    selectedDuration(chosenDuration);
                }
            });
        });

        setFirstEventDateRange();
        setSecondEventDateRange();

        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setText(dateTodayShorted);

        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setText(dateTodayShorted);

        firstEventListener();

        initBarChartSales();
        initPieChartSoldItems();

        btnSendMail = findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SalesForm.this, "receiverMail " + receiverEmail, Toast.LENGTH_SHORT).show();
                mailerService.setContext(SalesForm.this);
                mailerService.setReceiverEmail(receiverEmail);
                mailerService.sendMailTest();
            }
        });

    }

    private void initBarChartSales() {
        bChartSales = findViewById(R.id.bChartSales);
//        BarDataSet barDataSetSales = new BarDataSet(salesEntry(), "Sales");
        BarDataSet barDataSetSales = new BarDataSet(salesEntry(), "Sales");
        barDataSetSales.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSetSales.setValueTextColor(Color.BLACK);
        barDataSetSales.setValueTextSize(18f);
        BarData barDataSales = new BarData();

        barDataSales.addDataSet(barDataSetSales);
        bChartSales.setData(barDataSales);
        bChartSales.getDescription().setEnabled(true);
        bChartSales.invalidate();
    }

    private ArrayList<BarEntry> salesEntry() {
        ArrayList<BarEntry> salesEntry = new ArrayList<>();
        salesEntry.add(new BarEntry(2f, 13));
//        salesEntry.add(new BarEntry("12/01/2022",13));
        salesEntry.add(new BarEntry(3f, 25));
        salesEntry.add(new BarEntry(1f, 18));
        salesEntry.add(new BarEntry(4f, 14));
        salesEntry.add(new BarEntry(6f, 28));
        salesEntry.add(new BarEntry(5f, 22));
        return salesEntry;

    }

    private void initPieChartSoldItems() {
        pChartProducts = findViewById(R.id.pChartProducts);
        PieDataSet pieDataSetSales = new PieDataSet(soldItemsEntry(), "Top Sold Items");
        pieDataSetSales.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSetSales.setValueTextColor(Color.BLACK);
        pieDataSetSales.setValueTextSize(18f);

        PieData pieDataSales = new PieData();

        pieDataSales.addDataSet(pieDataSetSales);
        pChartProducts.setData(pieDataSales);
        pChartProducts.getDescription().setEnabled(true);
        pChartProducts.setCenterText("Top Sold Items");
        pChartProducts.animate();
        pChartProducts.invalidate();
    }

    private ArrayList<PieEntry> soldItemsEntry() {
        ArrayList<PieEntry> soldItemsEntry = new ArrayList<>();
        soldItemsEntry.add(new PieEntry(500, "2016"));
        soldItemsEntry.add(new PieEntry(600, "2017"));
        soldItemsEntry.add(new PieEntry(750, "2018"));
        soldItemsEntry.add(new PieEntry(800, "2019"));
        soldItemsEntry.add(new PieEntry(900, "2020"));
        soldItemsEntry.add(new PieEntry(1000, "2021"));
        return soldItemsEntry;
    }


    private void selectedDuration(String dateDuration) {

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
                        if (salesArrayList != null || (!salesArrayList.isEmpty())) {
                            tempSalesArrayList = salesArrayList;

                            if (salesArrayList.size() > 0) {
                                LocalDate start = LocalDate.parse(salesArrayList.get(0).getCreated_at());
                                LocalDate end = LocalDate.parse(salesArrayList.get(salesArrayList.size() - 1).getCreated_at());
                                startDateShort = dateTimeDateShort.format(start);
                                endDateShort = dateTimeDateShort.format(end);
                                btnStartDate.setText(startDateShort);
                                btnEndDate.setText(endDateShort);
                            }

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

            }
        });

    }

    private void setFirstEventDateRange() {
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
                    tempSalesArrayList = salesArrayList;

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
                    tempSalesArrayList = salesArrayList;

                }
            });
            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");
        });

    }

    private void firstEventListener() {
        View.OnClickListener buttonRangeClickListener = (View v) -> {
            switch (v.getId()) {
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

    private void secondEventListener() {
        View.OnClickListener buttonRangeClickListener = (View v) -> {
            switch (v.getId()) {
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

    private void setSecondEventDateRange() {
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
                    tempSalesArrayList = salesArrayList;

                }
            });

            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");

        });

    }

    private void initDialog() {
        durationChoiceDialog = new DurationChoiceDialog();
        durationChoiceDialog.setContext(SalesForm.this);
        dateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateTodayShorted = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        materialDatePickerStartDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerSingleDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerRangeStartDate = MDCDatePickerDialog.startDatePicker();
        materialDatePickerEndDate = MDCDatePickerDialog.endDatePicker(dateToday);
        materialDatePickerRangeEndDate = MDCDatePickerDialog.endDatePicker(dateToday);
    }

    private void removeFragmentDatePicker() {
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
            if (oldEndDate.isAdded()) {
                fragmentManager.beginTransaction().remove(oldEndDate).commit();
            }
        }

    }
}
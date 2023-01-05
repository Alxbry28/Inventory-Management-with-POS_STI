package com.example.inventorymanagementsystem.views.staff;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.dialogs.DurationChoiceDialog;
import com.example.inventorymanagementsystem.dialogs.MDCDatePickerDialog;
import com.example.inventorymanagementsystem.dialogs.SendMailDialog;
import com.example.inventorymanagementsystem.interfaces.IDurationChoiceDialogListener;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.example.inventorymanagementsystem.libraries.ExcelGenerator;
import com.example.inventorymanagementsystem.models.Sales;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.SoldItem;
import com.example.inventorymanagementsystem.models.SoldItemReport;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.services.MailerService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
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
    private User userOwner;
    private Staff staff;
    private String receiverEmail;
    private MailerService mailerService;
    private ExcelGenerator excelGenerator;

    private ArrayList<Sales> tempSalesArrayList;
    private ArrayList<SoldItem> tempSoldItemArrayList;
    private ArrayList<SoldItemReport> tempSoldItemReportsList;
    private File filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_form);

//        getSupportActionBar().hide();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        tempSalesArrayList = new ArrayList<>();

        tempSoldItemReportsList = new ArrayList<>();
        tempSoldItemArrayList = new ArrayList<>();

        mailerService = new MailerService();
        excelGenerator = new ExcelGenerator(SalesForm.this);

        fragmentManager = getSupportFragmentManager();
        currentDate = LocalDate.now();
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        sales = new Sales();
        staff = new Staff();
        userOwner = new User();
        initDialog();

        btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(v -> {
            if(Sales.FILE_PATH.exists()){
                Sales.FILE_PATH.delete();
            }
            startActivity(new Intent(SalesForm.this, HomeActivity.class));
            finish();
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

        pChartProducts = findViewById(R.id.pChartProducts);
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

        btnSendMail = findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener((View v) -> {
            excelGenerator = new ExcelGenerator(SalesForm.this);
            generateExcelDetails();
            boolean isGenerated = excelGenerator.generateSales();
            if (isGenerated) {
                SendMailDialog sendMailDialog = new SendMailDialog(SalesForm.this);
                sendMailDialog.setFilePath(excelGenerator.getFilePath());
                sendMailDialog.show(getSupportFragmentManager(), "DIALOG_SEND_EMAIL");
            }
            else{
                Toast.makeText(this, "Failed to generate", Toast.LENGTH_SHORT).show();
            }
        });

        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        btnGenerateReport.setOnClickListener((View v) -> {
                    excelGenerator = new ExcelGenerator(SalesForm.this);
                    generateExcelDetails();
                    boolean isGenerated = excelGenerator.generateSales();
                    if (isGenerated) {
                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(excelGenerator.getFilePath().getAbsolutePath());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "Failed to generate", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }


    private void generateExcelDetails() {
//        DateTimeFormatter todayDateFormatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        DateTimeFormatter dateGenFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        excelGenerator.setBusinessName(businessName.toUpperCase());
        excelGenerator.setStartDate(startDateShort);
        excelGenerator.setEndDate(endDateShort);
        excelGenerator.setTempSalesList(tempSalesArrayList);
        excelGenerator.setTempSoldItemList(tempSoldItemReportsList);
        excelGenerator.setDateGenerated(dateGenFormatter.format(LocalDate.now()) + " " + time.format(new Date()));
        excelGenerator.setFileNameUnique(businessName+"_"+f.format(new Date()));
    }

    private void populateBarChartSales(ArrayList<Sales> temp_SaleItemList, String duration) {
        bChartSales = findViewById(R.id.bChartSales);
        DateTimeFormatter dateFormatter;

        if (duration.equals("Last Year") || duration.equals("This Year") || duration.equals("Date Range")) {
            dateFormatter = DateTimeFormatter.ofPattern("MM_MMMyyyy", Locale.ENGLISH);
        } else if (duration.equals("All")) {
            dateFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
        } else {
            dateFormatter = DateTimeFormatter.ofPattern("MMM-dd-yy", Locale.ENGLISH);
        }

        Map<String, Double> tempSaleItemMap = new HashMap<>();
        for (Sales item : temp_SaleItemList) {
            LocalDate tempDate = LocalDate.parse(item.getCreated_at());
            String durationDate = dateFormatter.format(tempDate);

            tempSaleItemMap.computeIfPresent(durationDate, (s, v) -> v + item.getAmountPayable());
            tempSaleItemMap.putIfAbsent(durationDate, item.getAmountPayable());
        }

        BarData barDataSales = new BarData();
        Random rnd = new Random();
        int count = 1;

        List<Map.Entry<String, Double>> sortedTempSaleList = new ArrayList<>(tempSaleItemMap.entrySet());
        sortedTempSaleList.sort(Map.Entry.comparingByKey(Comparator.naturalOrder()));

        for (Map.Entry<String, Double> set : sortedTempSaleList) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            ArrayList<BarEntry> salesEntry = new ArrayList<>();
            salesEntry.add(new BarEntry(count, set.getValue().floatValue()));

            BarDataSet barDataSetSales1 = new BarDataSet(salesEntry, set.getKey().toString());
            barDataSetSales1.setColors(color);
            barDataSetSales1.setValueTextColor(Color.BLACK);
            barDataSetSales1.setValueTextSize(12f);
            barDataSales.addDataSet(barDataSetSales1);
            count++;
        }

        bChartSales.setData(barDataSales);
        bChartSales.getDescription().setEnabled(true);
        bChartSales.invalidate();
    }

    private void populatePieChartData(ArrayList<SoldItemReport> temp_SoldItemList) {
        Map<String, Double> tempSoldItemMap = new HashMap<>();
        for (SoldItemReport item : temp_SoldItemList) {
            tempSoldItemMap.computeIfPresent(item.getName(), (s, v) -> v + item.GetComputedSubtotal());
            tempSoldItemMap.putIfAbsent(item.getName(), item.GetComputedSubtotal());
        }

        ArrayList<PieEntry> soldItemsEntry = new ArrayList<>();
        for (Map.Entry<String, Double> set : tempSoldItemMap.entrySet()) {
            soldItemsEntry.add(new PieEntry(set.getValue().floatValue(), set.getKey().toString()));
        }

        String textLabel = (temp_SoldItemList.size() <= 0) ? "No Available Data" : "Top Sold Products";
        PieDataSet pieDataSetSales = new PieDataSet(soldItemsEntry, textLabel);
        pieDataSetSales.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSetSales.setValueTextColor(Color.BLACK);
        pieDataSetSales.setValueTextSize(18f);

        PieData pieDataSales = new PieData();

        pieDataSales.addDataSet(pieDataSetSales);
        pChartProducts.setData(pieDataSales);
        pChartProducts.getDescription().setEnabled(true);
        pChartProducts.setCenterText(textLabel);
        pChartProducts.animate();
        pChartProducts.invalidate();
    }

    private void selectedDuration(String dateDuration) {
        LocalDate firstDay, lastDay;
        SoldItemReport soldItem = new SoldItemReport();
        soldItem.setStoreId(storeId);

        switch (dateDuration) {
            case "Today":
                startDate = dateTimeFormatter.format(currentDate);
                endDate = startDate;
                startDateShort = dateTimeDateShort.format(currentDate);
                endDateShort = startDateShort;
                btnStartDate.setText(startDateShort);
                btnEndDate.setText(startDateShort);
                break;

            case "Last Day":
                lastDay = currentDate.minusDays(1);
                startDate = dateTimeFormatter.format(lastDay);
                endDate = startDate;
                startDateShort = dateTimeDateShort.format(lastDay);
                endDateShort = startDateShort;

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
                                List<Sales> sortedSales = salesArrayList.stream()
                                        .sorted(Comparator.comparing(Sales::getCreated_at)).collect(Collectors.toList());

                                LocalDate start = LocalDate.parse(sortedSales.get(0).getCreated_at());
                                LocalDate end = LocalDate.parse(sortedSales.get(salesArrayList.size() - 1).getCreated_at());
                                startDateShort = dateTimeDateShort.format(start);
                                endDateShort = dateTimeDateShort.format(end);
                                btnStartDate.setText(startDateShort);
                                btnEndDate.setText(endDateShort);
                                populateBarChartSales(salesArrayList, dateDuration);
                            }

                        }
                    }
                });

                soldItem.GetAll(new IEntityModelListener<SoldItemReport>() {
                    @Override
                    public void retrieve(SoldItemReport m) {

                    }

                    @Override
                    public void getList(ArrayList<SoldItemReport> soldItemReports) {
                        tempSoldItemReportsList = soldItemReports;
                        populatePieChartData(soldItemReports);
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

                    startDateShort = formattedShortDate;
                    endDateShort = formattedShortDate;

                    startDate = formattedDate;
                    endDate = formattedDate;

                    getSalesAndSoldItemData(startDate, endDate, dateDuration);

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

        getSalesAndSoldItemData(startDate, endDate, dateDuration);

    }

    private void getSalesAndSoldItemData(String startDate, String endDate, String duration) {
        SoldItemReport soldItemReport = new SoldItemReport();
        soldItemReport.setStoreId(storeId);
        soldItemReport.GetAllByDateRange(startDate, endDate, new IEntityModelListener<SoldItemReport>() {
            @Override
            public void retrieve(SoldItemReport m) {

            }

            @Override
            public void getList(ArrayList<SoldItemReport> soldItemReports) {
                tempSoldItemReportsList = soldItemReports;
                populatePieChartData(soldItemReports);
            }
        });

        sales.GetAllByDateRange(startDate, endDate, new IEntityModelListener<Sales>() {
            @Override
            public void retrieve(Sales m) {

            }

            @Override
            public void getList(ArrayList<Sales> salesArrayList) {
                tempSalesArrayList = salesArrayList;
                populateBarChartSales(salesArrayList, duration);
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

            getSalesAndSoldItemData(startDate, endDate, "Date Range");

            btnStartDate.setText(startDateShort);
            btnEndDate.setText(endDateShort);
            btnSelectDuration.setText("Date Range");
        });

        materialDatePickerEndDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            endDate = simpleDateFormatter.format(calendar.getTime());
            endDateShort = simpleDateFormatterShort.format(calendar.getTime());
            getSalesAndSoldItemData(startDate, endDate, "Date Range");
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
            getSalesAndSoldItemData(startDate, endDate, "Date Range");
            btnSelectDuration.setText("Date Range");
            removeFragmentDatePicker();
            btnEndDate.callOnClick();
        });

        materialDatePickerRangeEndDate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            calendar.setTimeInMillis(selection);
            endDate = simpleDateFormatter.format(calendar.getTime());
            endDateShort = simpleDateFormatterShort.format(calendar.getTime());
            getSalesAndSoldItemData(startDate, endDate, "Date Range");

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
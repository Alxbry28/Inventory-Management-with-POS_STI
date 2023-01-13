package com.example.inventorymanagementsystem.views.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.dialogs.DurationChoiceDialog;
import com.example.inventorymanagementsystem.dialogs.MDCDatePickerDialog;
import com.example.inventorymanagementsystem.dialogs.SendMailDialog;
import com.example.inventorymanagementsystem.interfaces.IDurationChoiceDialogListener;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.ExcelGenerator;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;
import com.example.inventorymanagementsystem.models.SoldItemReport;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.services.MailerService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.example.inventorymanagementsystem.R;
public class InventoryForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private RecyclerView rcInventory;
    private Button btnBack, btnSelectDuration, btnStartDate, btnEndDate;
    private Button btnSendMail, btnGenerateReport;


    private DurationChoiceDialog durationChoiceDialog;
    private String startDate, endDate, startDateShort, endDateShort;
    private String dateToday, dateTodayShorted;
    private MaterialDatePicker<Long> materialDatePickerStartDate, materialDatePickerEndDate;
    private MaterialDatePicker<Long> materialDatePickerRangeStartDate, materialDatePickerRangeEndDate;
    private MaterialDatePicker<Long> materialDatePickerSingleDate, materialDatePickerRangeDate;
    private SimpleDateFormat simpleDateFormatter;
    private SimpleDateFormat simpleDateFormatterShort;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter dateTimeDateShort;
    private LocalDate currentDate;
    private FragmentManager fragmentManager;
    private TextView tvEmptyTransaction;
    private Sales sales;
    private User userOwner;
    private Staff staff;
    private Product product;
    private ArrayList<Product> productList;
    private String receiverEmail;
    private MailerService mailerService;
    private ExcelGenerator excelGenerator;
    private ArrayList<SoldItemReport> tempSoldItemReportsList;
    private ArrayList<Sales> tempSalesArrayList;
    private ArrayList<SoldItem> tempSoldItemArrayList;
    private ArrayList<Product> tempProductArrayList;
    private File filePath;
    private TextView tvEmptyInventory, tvProductsNum, tvCategoryNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormatterShort = new SimpleDateFormat("dd MMM yyyy");

        currentDate = LocalDate.now();
        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);

        mailerService = new MailerService();
        excelGenerator = new ExcelGenerator(InventoryForm.this);

        tempProductArrayList = new ArrayList<>();
        tempSalesArrayList = new ArrayList<>();
        tempSoldItemReportsList = new ArrayList<>();
        tempSoldItemArrayList = new ArrayList<>();

        sales = new Sales();
        staff = new Staff();
        userOwner = new User();
        product = new Product();

        fragmentManager = getSupportFragmentManager();

        staff.setStoreId(storeId);
        sales.setStoreId(storeId);
        product.setStoreId(storeId);

        initDialog();

        initComponents();

        selectedDuration(btnSelectDuration.getText().toString());

        initEvents();
    }


    private void initComponents(){
        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setText(dateTodayShorted);
        btnSelectDuration = findViewById(R.id.btnSelectDuration);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setText(dateTodayShorted);

        btnBack = findViewById(R.id.btnback);
        btnSendMail = findViewById(R.id.btnSendMail);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        tvProductsNum = findViewById(R.id.tvProductsNumber);
        tvCategoryNum = findViewById(R.id.tvCategoryNum);
    }

    private void initEvents(){
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(InventoryForm.this, HomeActivity.class));
            finish();
        });
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

        btnSendMail.setOnClickListener((View v) -> {
            excelGenerator = new ExcelGenerator(InventoryForm.this);
            generateExcelDetails();
            boolean isGenerated = excelGenerator.generateInventory();
            if (isGenerated) {
                SendMailDialog sendMailDialog = new SendMailDialog(InventoryForm.this);
                sendMailDialog.setReportType(2);
                sendMailDialog.setReportType(2);
                sendMailDialog.setFilePath(excelGenerator.getFilePath());
                sendMailDialog.show(getSupportFragmentManager(), "DIALOG_SEND_EMAIL");
            }
            else{
                Toast.makeText(this, "Failed to generate", Toast.LENGTH_SHORT).show();
            }
        });

        btnGenerateReport.setOnClickListener((View v) -> {
            excelGenerator = new ExcelGenerator(InventoryForm.this);
            generateExcelDetails();
            boolean isGenerated = excelGenerator.generateInventory();
            if (isGenerated) {
                Toast.makeText(this, "Inventory Report Successfully Generated.", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(excelGenerator.getFilePath().getAbsolutePath());

                Uri mydir = Uri.parse(excelGenerator.getFolderDocument().getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(mydir, DocumentsContract.Document.MIME_TYPE_DIR);
                startActivity(Intent.createChooser(intent, "Open Folder"));
            }
            else{
                Toast.makeText(this, "Failed to generate", Toast.LENGTH_SHORT).show();
            }
        });

        setFirstEventDateRange();
        setSecondEventDateRange();
        firstEventListener();
    }

    private void generateExcelDetails() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        DateTimeFormatter dateGenFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        excelGenerator.setBusinessName(businessName.toUpperCase());
        excelGenerator.setStartDate(startDateShort);
        excelGenerator.setEndDate(endDateShort);
        excelGenerator.setProductList(tempProductArrayList);
        excelGenerator.setTempSalesList(tempSalesArrayList);
        excelGenerator.setTempSoldItemList(tempSoldItemReportsList);
        excelGenerator.setDateGenerated(dateGenFormatter.format(LocalDate.now()) + " " + time.format(new Date()));
        excelGenerator.setFileNameUnique(businessName+"_"+f.format(new Date()));
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

                product.GetAll(new ProductModelListener() {
                    @Override
                    public void retrieveProduct(Product p) {

                    }

                    @Override
                    public void getProductList(ArrayList<Product> productArrayList) {
                        if (productArrayList != null || (!productArrayList.isEmpty())) {
                            if (productArrayList.size() > 0) {
                                List<Product> sortedProduct= productArrayList.stream()
                                        .sorted(Comparator.comparing(Product::getUpdated_at)).collect(Collectors.toList());

                                LocalDate start = LocalDate.parse(sortedProduct.get(0).getUpdated_at());
                                LocalDate end = LocalDate.parse(sortedProduct.get(productArrayList.size() - 1).getUpdated_at());
                                startDateShort = dateTimeDateShort.format(start);
                                endDateShort = dateTimeDateShort.format(end);
                                btnStartDate.setText(startDateShort);
                                btnEndDate.setText(endDateShort);

                                productList = productArrayList;

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
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();
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
            }
        });

        product.GetAllByDateRange(startDate, endDate, new ProductModelListener() {
            @Override
            public void retrieveProduct(Product p) {

            }

            @Override
            public void getProductList(ArrayList<Product> productArrayList) {
                tempProductArrayList = productArrayList;

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
        durationChoiceDialog.setContext(InventoryForm.this);
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
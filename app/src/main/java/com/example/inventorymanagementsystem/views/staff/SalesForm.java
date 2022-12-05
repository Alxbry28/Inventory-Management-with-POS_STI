package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.inventorymanagementsystem.dialogs.MDCDatePickerDialog;
import com.example.inventorymanagementsystem.models.Sales;

import java.util.ArrayList;

import com.example.inventorymanagementsystem.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class SalesForm extends AppCompatActivity {

    private Button btnBack, btnStartDate, btnEndDate;
    private PieChart pChartProducts;
    private BarChart bChartSales;
    private ArrayList<Sales> salesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_form);

        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);




        MaterialDatePicker materialDatePicker = MDCDatePickerDialog.openDatePicker();
        MaterialDatePicker materialDateRangePicker = MDCDatePickerDialog.openDateRangePicker();

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDateRangePicker.show(getSupportFragmentManager(), "DATE_PICKER_RANGE");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Toast.makeText(SalesForm.this, "selected date: " + materialDatePicker.getHeaderText(), Toast.LENGTH_SHORT).show();
            }
        });

        materialDateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Toast.makeText(SalesForm.this, "selected date: " + materialDateRangePicker.getHeaderText(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(SalesForm.this, HomeActivity.class));
        });
        initBarChartSales();
    }

    private void initBarChartSales(){
        bChartSales = findViewById(R.id.bChartSales);
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

    private ArrayList<BarEntry> salesEntry(){
        ArrayList<BarEntry> salesEntry = new ArrayList<>();
        salesEntry.add(new BarEntry(2f,13));
//        salesEntry.add(new BarEntry("12/01/2022",13));
        salesEntry.add(new BarEntry(3f,25));
        salesEntry.add(new BarEntry(1f,18));
        salesEntry.add(new BarEntry(4f,14));
        salesEntry.add(new BarEntry(6f,28));
        salesEntry.add(new BarEntry(5f,22));
        return salesEntry;

    }

}
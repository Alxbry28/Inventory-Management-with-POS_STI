package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventorymanagementsystem.adapters.SalesRCVAdapter;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.Sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SalesForm extends AppCompatActivity {

    Button back;

    private ArrayList<Sales> salesArrayList;
    private RecyclerView rcSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_form);
        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

        rcSales = findViewById(R.id.rcSales);

        salesArrayList = new ArrayList<>();
        Sales sales1 = new Sales();
        sales1.setCreatedAt("Oct 26, 2022");
        sales1.setTotal_price(100);
        sales1.setQuantity(10);
        sales1.setId(1);

        Sales sales2 = new Sales();
        sales2.setCreatedAt("Oct 27, 2022");
        sales2.setTotal_price(200);
        sales2.setQuantity(10);
        sales2.setId(3);

        Sales sales3 = new Sales();
        sales3.setCreatedAt("Oct 28, 2022");
        sales3.setTotal_price(150);
        sales3.setQuantity(9);
        sales3.setId(2);

        Sales sales4 = new Sales();
        sales4.setCreatedAt("Oct 26, 2022");
        sales4.setTotal_price(100);
        sales4.setQuantity(10);
        sales4.setId(4);

        salesArrayList.add(sales1);
        salesArrayList.add(sales2);
        salesArrayList.add(sales3);
        salesArrayList.add(sales4);

        SalesRCVAdapter salesRCVAdapter = new SalesRCVAdapter();
        salesRCVAdapter.setSalesList(salesArrayList);

        rcSales.setAdapter(salesRCVAdapter);

        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(SalesForm.this);
        rcSales.setLayoutManager(rcvLayoutManager);
        rcSales.setItemAnimator(new DefaultItemAnimator());

        back = (Button)findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalesForm.this, HomeActivity.class));
            }
        });
    }
    private String time ()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
    private String date ()
    {
        return new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(new Date());
    }
}
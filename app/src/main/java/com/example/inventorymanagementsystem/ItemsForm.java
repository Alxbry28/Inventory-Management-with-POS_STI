package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventorymanagementsystem.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemsForm extends AppCompatActivity {


    Button back, Categories, Items;
    private Button btnAddProduct;
    private TextView tvEmptyProductMsg;
    private RecyclerView rcProducts;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_form);

        tvEmptyProductMsg = findViewById(R.id.tvEmptyProductMsg);
        rcProducts = findViewById(R.id.rcProducts);

        productList = new ArrayList<>();

        Product product1 = new Product();
        product1.setName("Buko");
        product1.setPrice(25);
        product1.setQuantity(20);
        product1.setId("1");

        productList.add(product1);

        if(productList == null || productList.isEmpty()){
            tvEmptyProductMsg.setVisibility(View.VISIBLE);
            rcProducts.setVisibility(View.INVISIBLE);
        }



        back = (Button)findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemsForm.this, HomeActivity.class));
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
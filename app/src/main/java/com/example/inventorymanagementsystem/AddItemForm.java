package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.models.Product;

import java.io.Serializable;
import java.util.HashMap;

public class AddItemForm extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private TextView tvBusinessName;
    private EditText etItem, etProductName, etProductCategory, etPrice, etStocks;
    private Button btnAdd, btnBack;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_form);
        product = new Product();
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        initComponents();
        initEventButtons();

        tvBusinessName.setText(businessName);
    }

    private void initComponents(){
        btnBack = findViewById(R.id.btnBackToProducts);
        btnAdd = findViewById(R.id.btnAddItemProduct);

        tvBusinessName = findViewById(R.id.tv1);
        etProductName = findViewById(R.id.etProductName);
        etProductCategory = findViewById(R.id.etProductCategory);
        etStocks = findViewById(R.id.etStocks);
        etPrice = findViewById(R.id.etPrice);
    }

    private void initEventButtons(){
        btnAdd.setOnClickListener(v ->{

            product.setStoreId(storeId);
            product.setUserId(userId);
            product.setQuantity(Integer.parseInt(etStocks.getText().toString()));
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));
            product.setName(etProductName.getText().toString());
            product.setCategory(etProductCategory.getText().toString());
            product.Create(status -> {
                if(status){
                    Toast.makeText(AddItemForm.this, "Successfully add product", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        });

        btnBack.setOnClickListener(v ->{
            finish();
        });
    }



}
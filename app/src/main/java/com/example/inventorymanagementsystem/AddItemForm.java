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

import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.Validation;
import com.example.inventorymanagementsystem.models.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AddItemForm extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId, productId;
    private TextView tvBusinessName,tvProductTransactionType;
    private EditText etItem, etProductName, etProductCategory, etPrice, etStocks;
    private Button btnAdd, btnBack;
    private Product product;
    private boolean isEditProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_form);

		// edit product
		boolean isEditProduct = getIntent().getBooleanExtra("isEditProduct", false);
		
        product = new Product();
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        initComponents();
        isEditProduct = getIntent().hasExtra("isEditProduct");
        Toast.makeText(AddItemForm.this, "isEditProduct " + isEditProduct, Toast.LENGTH_SHORT).show();
        if(isEditProduct){
            product.setId(getIntent().getStringExtra("productId"));
            product.GetById(new ProductModelListener() {
                @Override
                public void retrieveProduct(Product product) {
                    etProductName.setText(product.getName());
                    etProductCategory.setText(product.getCategory());
                    etPrice.setText(String.valueOf(product.getPrice()));
                    etStocks.setText(String.valueOf(product.getQuantity()));
                    etStocks.setEnabled(false);
                }

                @Override
                public void getProductList(ArrayList<Product> productArrayList) {

                }
            });
            String transType = "Edit Product";
            Toast.makeText(this, transType, Toast.LENGTH_SHORT).show();
            tvProductTransactionType.setText(transType);
            btnAdd.setText("Save");
        }
        initEventButtons();

        tvBusinessName.setText(businessName);
    }

    private void initComponents(){
        tvProductTransactionType = findViewById(R.id.tvProductTransactionType);
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

            if(!Validation.isValueDouble(etPrice.getText().toString())){
                Toast.makeText(AddItemForm.this, "Price is not valid", Toast.LENGTH_SHORT).show();
                return;
            }

            isEditProduct = getIntent().hasExtra("isEditProduct");

            product.setStoreId(storeId);
            product.setUserId(userId);

            product.setQuantity(Integer.parseInt(etStocks.getText().toString()));
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));
            product.setName(etProductName.getText().toString());
            product.setCategory(etProductCategory.getText().toString());

            if(isEditProduct){
                product.Update(status -> {
                    if (status){
                        Toast.makeText(AddItemForm.this, "Saved Product successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            else{
                product.Create(status -> {
                    if(status){
                        Toast.makeText(AddItemForm.this, "Successfully add product", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }


        });

        btnBack.setOnClickListener(v ->{
            finish();
        });
    }



}
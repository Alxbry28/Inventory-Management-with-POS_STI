package com.example.inventorymanagementsystem.views.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.List;

import com.example.inventorymanagementsystem.R;
public class InventoryForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private RecyclerView rcInventory;
    private Button btnBack,btnAddProduct;
    private TextView tvEmptyInventory;
    private Product product;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);
//        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);

        product = new Product();
        product.setStoreId(storeId);
        product.GetAll(new ProductModelListener() {
            @Override
            public void retrieveProduct(Product product) {

            }

            @Override
            public void getProductList(ArrayList<Product> productArrayList) {
                productList = productArrayList;
                Toast.makeText(InventoryForm.this, "productList: " + productList.size(), Toast.LENGTH_SHORT).show();
            }
        });

        tvEmptyInventory = findViewById(R.id.tvEmptyInventory);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryForm.this, HomeActivity.class));
                finish();
            }
        });

        rcInventory = findViewById(R.id.rcInventory);
        rcInventory.setHasFixedSize(true);

    }

}
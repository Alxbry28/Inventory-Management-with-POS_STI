package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.ProductRCVAdapter;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemsForm extends AppCompatActivity {

    Button back, Categories, Items;
    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private Button btnAddProduct;
    private TextView tvEmptyProductMsg;
    private RecyclerView rcProducts;
    private ArrayList<Product> productList;
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_form);
        product = new Product();
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);
        product.setUserId(userId);
        product.setStoreId(storeId);

        product.GetAll(new ProductModelListener() {
            @Override
            public void retrieveProduct(Product product) {


            }

            @Override
            public void getProductList(ArrayList<Product> productArrayList) {
                if(!productArrayList.isEmpty()){
                    productList = productArrayList;
                    if(!(productList == null || productList.isEmpty())){
                        tvEmptyProductMsg.setVisibility(View.INVISIBLE);
                        rcProducts.setVisibility(View.VISIBLE);

                        ProductRCVAdapter productRCVAdapter = new ProductRCVAdapter();
                        productRCVAdapter.setContext(ItemsForm.this);
                        productRCVAdapter.setProductList(productList);

                        rcProducts.setAdapter(productRCVAdapter);
                        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(ItemsForm.this);
                        rcProducts.setLayoutManager(rcvLayoutManager);
                        rcProducts.setItemAnimator(new DefaultItemAnimator());

                    }
                }
            }
        });

        tvEmptyProductMsg = findViewById(R.id.tvEmptyProductMsg);
        rcProducts = findViewById(R.id.rcProducts);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v->{
            startActivity(new Intent(ItemsForm.this, AddItemForm.class));
        });

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
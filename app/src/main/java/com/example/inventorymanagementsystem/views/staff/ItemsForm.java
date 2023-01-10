package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.POSRCVAdapter;
import com.example.inventorymanagementsystem.adapters.ProductRCVAdapter;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
public class ItemsForm extends AppCompatActivity {

    Button back, Categories, Items;
    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private Button btnAddProduct;
    private TextView tvEmptyProductMsg;
    private RecyclerView rcProducts;
    private ArrayList<Product> productList;
    private Product product;
    private EditText etSearch;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_form);
//        getSupportActionBar().hide();
        product = new Product();
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);
        userType = sharedPreferences.getInt("userType", 0);

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

                        initRCVProductsItem(productArrayList);

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

        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                search(search);
            }
        });
    }

    private void search(String search) {
        Product tempProduct = new Product();
        tempProduct.setUserId(userId);
        tempProduct.setStoreId(storeId);

        if (TextUtils.isEmpty(search)) {
            tempProduct.GetAll(new ProductModelListener() {
                @Override
                public void retrieveProduct(Product product) {

                }

                @Override
                public void getProductList(ArrayList<Product> productArrayList) {
                    if(!productArrayList.isEmpty()){
                        productList = productArrayList;
                        if(!(productList == null || productList.isEmpty())){
                            initRCVProductsItem(productArrayList);
                        }
                    }
                }
            });
        } else {
            tempProduct.Search(search,new ProductModelListener() {
                @Override
                public void retrieveProduct(Product product) {


                }

                @Override
                public void getProductList(ArrayList<Product> productArrayList) {
                    if(!productArrayList.isEmpty()){
                        productList = productArrayList;
                        if(!(productList == null || productList.isEmpty())){
                            initRCVProductsItem(productArrayList);
                        }
                    }
                }
            });
        }

    }

    private void initRCVProductsItem(ArrayList<Product> productArrayList){
        ProductRCVAdapter productRCVAdapter = new ProductRCVAdapter();
        productRCVAdapter.setContext(ItemsForm.this);
        productRCVAdapter.setStaffRole(userType);
//        productRCVAdapter.setProductList(productArrayList);
        List<Product> tempsortedProduct = productArrayList.stream()
                .sorted((p1,p2)-> p1.getName().toUpperCase().compareTo(p2.getName().toUpperCase()))
                .collect(Collectors.toList());
        ArrayList<Product> sortedProduct = new ArrayList<>(tempsortedProduct);
        productRCVAdapter.setProductList(sortedProduct);

        rcProducts.setAdapter(productRCVAdapter);
        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(ItemsForm.this);
        rcProducts.setLayoutManager(rcvLayoutManager);
        rcProducts.setItemAnimator(new DefaultItemAnimator());
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
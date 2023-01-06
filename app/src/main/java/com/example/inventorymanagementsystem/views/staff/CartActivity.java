package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.CartItemRCVAdapter;
import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.interfaces.CartItemsListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.inventorymanagementsystem.R;

public class CartActivity extends AppCompatActivity {

    private TextView tvTotalPrice, tvTotalItems;
    private Button btnCheckout,btnBack;
    private ArrayList<CartItem> cartItemsArrayList;
    private CartLibrary cartLibrary;
    private RecyclerView rcCartItems;
    private double totalPrice = 0;
    private int totalQty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
//        getSupportActionBar().hide();
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((View v) -> {
                startActivity(new Intent(CartActivity.this, POSItemActivity.class));
                finish();
            }
        );

        btnCheckout = findViewById(R.id.btnCheckout);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        rcCartItems = findViewById(R.id.rcCartItems);

        cartLibrary = new CartLibrary();
        cartLibrary.setSqLiteDB(new SQLiteDB(this));
        cartItemsArrayList = cartLibrary.retrieveCartItems();

        CartItemRCVAdapter cartItemRCVAdapter = new CartItemRCVAdapter();
        cartItemRCVAdapter.setContext(CartActivity.this);
        cartItemRCVAdapter.setCartItemList(cartItemsArrayList);

        rcCartItems.setAdapter(cartItemRCVAdapter);

        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(CartActivity.this);
        rcCartItems.setLayoutManager(rcvLayoutManager);
        rcCartItems.setItemAnimator(new DefaultItemAnimator());

        cartLibrary.setCartItemArrayList(cartItemsArrayList);
        cartItemRCVAdapter.setOnQuantityChange(cartItemsList-> {
            cartItemsArrayList = cartItemsList;
            showTotalPrice();
                }
        );
        showTotalPrice();

        btnCheckout.setOnClickListener(v->{
            //Toast.makeText(this, "Payment", Toast.LENGTH_SHORT).show();
            cartLibrary.clear();
            cartLibrary.setCartItemArrayList(cartItemsArrayList);
            cartLibrary.saveCartItems();
            startActivity(new Intent(CartActivity.this,PaymentActivity.class));
        });
    }

    private void showTotalPrice(){
        tvTotalPrice.setText("Total: P" +cartLibrary.totalCartPrice());
        tvTotalItems.setText("Items: " + cartLibrary.totalCartItems());
        if(cartLibrary.totalCartItems() == 0 || cartLibrary.totalCartPrice() == 0){
           btnCheckout.setEnabled(false);
        }

    }

}
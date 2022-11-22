package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.CartItemRCVAdapter;
import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ArrayList<CartItem> cartItemsArrayList;
    private CartLibrary cartLibrary;
    private RecyclerView rcCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        Toast.makeText(this, "cartItemsArrayList " + cartItemsArrayList.size(), Toast.LENGTH_SHORT).show();
    }
}
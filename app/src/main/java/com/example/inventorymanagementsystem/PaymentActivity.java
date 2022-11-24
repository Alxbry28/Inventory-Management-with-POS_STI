package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.CartItem;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private ArrayList<CartItem> cartItemsArrayList;
    private CartLibrary cartLibrary;
    private double totalPrice = 0;
    private int totalQty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
}
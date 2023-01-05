package com.example.inventorymanagementsystem.interfaces;

import com.example.inventorymanagementsystem.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public interface CartItemsListener {
    void retrieveCartItemList(ArrayList<CartItem> cartItemsList);
}

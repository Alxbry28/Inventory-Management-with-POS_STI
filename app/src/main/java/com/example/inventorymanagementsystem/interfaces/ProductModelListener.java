package com.example.inventorymanagementsystem.interfaces;

import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public interface ProductModelListener {
    void retrieveProduct(Product p);
    void getProductList(ArrayList<Product> productArrayList);
}

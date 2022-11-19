package com.example.inventorymanagementsystem.libraries;

import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public class CartLibrary {

    private SQLiteDB sqLiteDB;
    private ArrayList<Product> productArrayList;

    public SQLiteDB getSqLiteDB() {
        return sqLiteDB;
    }

    public void setSqLiteDB(SQLiteDB sqLiteDB) {
        this.sqLiteDB = sqLiteDB;
    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

}

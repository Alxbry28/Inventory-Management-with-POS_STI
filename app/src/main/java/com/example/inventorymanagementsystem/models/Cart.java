package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.database.SQLiteDB;

public class Cart {
    private static final String TABLE_NAME = "tblCart";
    private SQLiteDB sqLiteDB;

    public static String initTable(){
        return "CREATE TABLE "+ TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, productId INT, quantity INT, price REAL, created_at TEXT default current_timestamp, updated_at TEXT default current_timestamp)";
    }

    public static String dropTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

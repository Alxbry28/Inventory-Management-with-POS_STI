package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.database.SQLiteDB;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private static final String TABLE_NAME = "tblCart";
    private SQLiteDB sqLiteDB;
    private int id, quantity;
    private double price, totalPrice;
    private String productId;
    private String created_at, updated_at;

    public boolean Create(){
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();
        String sql = "INSERT INTO "+TABLE_NAME+"(productId, quantity, price, totalPrice) VALUES(?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, productId);
        statement.bindLong(2, quantity);
        statement.bindDouble(3, price);
        statement.bindDouble(4, totalPrice);
        return statement.executeInsert() > 0;
    }

    public boolean ClearAll(){
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME+"";
        SQLiteStatement statement = db.compileStatement(sql);
        return statement.executeUpdateDelete() > 0;
    }

    public static String initTable(){
        return "CREATE TABLE "+ TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, productId INT, quantity INT, price REAL, totalPrice REAL, created_at TEXT default current_timestamp, updated_at TEXT default current_timestamp)";
    }

    public static String dropTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public SQLiteDB getSqLiteDB() {
        return sqLiteDB;
    }

    public void setSqLiteDB(SQLiteDB sqLiteDB) {
        this.sqLiteDB = sqLiteDB;
    }
}

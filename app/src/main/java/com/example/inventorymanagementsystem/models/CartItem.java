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
    private String productId, category, name;
    private String created_at, updated_at;

    public boolean Create(){
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();
        String sql = "INSERT INTO "+TABLE_NAME+"(productId, quantity, price, totalPrice, category, name) VALUES(?,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, productId);
        statement.bindLong(2, quantity);
        statement.bindDouble(3, price);
        statement.bindDouble(4, totalPrice);
        statement.bindString(5, category);
        statement.bindString(6, name);
        return statement.executeInsert() > 0;
    }

    public ArrayList<CartItem> GetAll(){
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME +" ";
        Cursor c = db.rawQuery(sql, null);
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        if (c.getCount() < 0){
            return null;
        }
        else{
            while(c.moveToNext()){
                CartItem cartItem = new CartItem();
                cartItem.setId(c.getInt(c.getColumnIndex("id")));
                cartItem.setProductId(c.getString(c.getColumnIndex("productId")));
                cartItem.setName(c.getString(c.getColumnIndex("name")));
                cartItem.setPrice(c.getDouble(c.getColumnIndex("price")));
                cartItem.setQuantity(c.getInt(c.getColumnIndex("quantity")));
                cartItem.setCategory(c.getString(c.getColumnIndex("category")));
                cartItem.setTotalPrice(c.getDouble(c.getColumnIndex("totalPrice")));
                cartItem.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                cartItem.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));
                cartItemList.add(cartItem);
            }
            return cartItemList;
        }
    }

    //Ex: Delete By Id
    public boolean DeleteById(){
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME+" WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,id);
        return statement.executeUpdateDelete() > 0;
    }

    public boolean ClearAll(){
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME+"";
        SQLiteStatement statement = db.compileStatement(sql);
        return statement.executeUpdateDelete() > 0;
    }

    public static String initTable(){
        return "CREATE TABLE "+ TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, productId INT, name TEXT, category TEXT, quantity INT, price REAL, totalPrice REAL, created_at TEXT default current_timestamp, updated_at TEXT default current_timestamp)";
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double GetComputedTotalPrice(){
        return quantity * price;
    }

    public double getTotalPrice(){
        double roundOffPrice = (double) Math.round(totalPrice * 100) / 100;
        return roundOffPrice;
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

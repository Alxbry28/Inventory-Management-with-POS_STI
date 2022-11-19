package com.example.inventorymanagementsystem.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.inventorymanagementsystem.models.Cart;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final int version = 3;
    private static final SQLiteDatabase.CursorFactory factory = null;
    private static final String name = "InventoryManagementDB.db";

    public SQLiteDB(@Nullable Context context) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Cart.initTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Cart.dropTable());
        onCreate(db);
    }

    //For DROP TABLE, CREATE TABLE, DELETE TABLE
    public void queryWritable(String sql){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    //FOR SELECT QUERY
    public Cursor queryReadable(String sql, String[] args){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, args);
        return c;
    }

    //To delete all data from the table
    public boolean deleteTableData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        int isDeleted = db.delete(tableName,null, null);
        db.close();
        return isDeleted  > 0;
    }

}


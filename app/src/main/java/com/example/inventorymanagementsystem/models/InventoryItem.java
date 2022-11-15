package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.database.RealtimeFirebaseDB;
import com.google.firebase.database.DatabaseReference;

public class InventoryItem {

    private String id, productId, storeId;
    private int stocks, lowStock;

    public static final String TABLE = "tblInventory";
    private RealtimeFirebaseDB realtimeFirebaseDB;
    private DatabaseReference dbRef;

    public InventoryItem(){
        this.realtimeFirebaseDB = new RealtimeFirebaseDB();
        this.dbRef = realtimeFirebaseDB.InventoryTable();
    }
}

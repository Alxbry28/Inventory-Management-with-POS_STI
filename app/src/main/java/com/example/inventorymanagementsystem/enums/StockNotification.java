package com.example.inventorymanagementsystem.enums;

import com.example.inventorymanagementsystem.R;

public enum StockNotification {

    NOSTOCK("Need to re order items", R.drawable.ic_baseline_cancel_24),
    RESTOCK("Almost need to re order items",R.drawable.ic_baseline_error_24),
    GOOD("All stocks are in Good Condition",R.drawable.ic_baseline_check_circle_24);

    public String Message;
    public int Icon;

    StockNotification(String message, int icon){
        this.Message = message;
        this.Icon = icon;
    }

}

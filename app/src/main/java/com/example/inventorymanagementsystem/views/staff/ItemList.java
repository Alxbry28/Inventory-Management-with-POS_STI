package com.example.inventorymanagementsystem.views.staff;

import com.google.firebase.database.Exclude;

public class ItemList {

    @Exclude
    private String key;
    private String itemname;
    private String quantity;
    private String price;
    private String measurement;

    public ItemList(){}
    public ItemList(String itemname, String quantity, String price, String measurement) {
        this.itemname = itemname;
        this.quantity = quantity;
        this.price = price;
        this.measurement = measurement;
    }

    public String getItemname()
    {
        return itemname;
    }

    public void setItemname(String itemname)
    {
        this.itemname = itemname;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public String getPrice()

    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getMeasurement()
    {
        return measurement;
    }

    public void setMeasurement(String measurement)
    {
        this.measurement = measurement;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

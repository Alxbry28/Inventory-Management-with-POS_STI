package com.example.inventorymanagementsystem.interfaces;

import com.example.inventorymanagementsystem.models.Sales;
import java.util.ArrayList;

public interface SalesModelListener {
    void retrieveSales(Sales sales);
    void getSalesList(ArrayList<Sales> salesArrayList);
}

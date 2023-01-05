package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;

// Business Owner
public class BusinessOwner extends Staff{
    public BusinessOwner() {
        super();
        super.setPosition("Business Owner");
    }
}

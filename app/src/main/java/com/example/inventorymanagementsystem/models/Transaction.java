package com.example.inventorymanagementsystem.models;

import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.IModelRepository;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;

public class Transaction implements IModelRepository<Transaction> {

    private int id, product_id, quantity;
    private String customerName;
    private String typeOfPayment;
    private double total_price;
    private String status, createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void Create(TransactionStatusListener transactionStatus) {

    }

    @Override
    public void Update(TransactionStatusListener transactionStatus) {

    }

    @Override
    public void Delete(TransactionStatusListener transactionStatus) {

    }

    @Override
    public void GetById(IEntityModelListener<Transaction> entityModelListener) {

    }

    @Override
    public void GetAll(IEntityModelListener<Transaction> entityModelListener) {

    }

}

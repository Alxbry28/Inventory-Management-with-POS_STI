package com.example.inventorymanagementsystem.interfaces;

import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public interface IEntityModelListener<Model> {
    void retrieve(Model m);
    void getList(ArrayList<Model> modelArrayList);
}

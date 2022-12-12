package com.example.inventorymanagementsystem;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SalesFormTest {

    @Test
    public void soldItemsTest1() {
//        SoldItem(String name, String category, int quantity, double productPrice, String created_at, String created_time)
        ArrayList<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(new SoldItem("Sprite", "Softdrinks", 2, 20, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Tapsilog", "Food", 2, 45, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Nova", "Snacks", 2, 19, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Coke", "Softdrinks", 3, 15, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 2, 15, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11", "06:37 PM"));

        Map<String, Double> tempSoldItem = new HashMap<>();
        for (SoldItem item : soldItems) {
            tempSoldItem.computeIfPresent(item.getName(), (s, v) -> v + item.GetComputedSubtotal());
            tempSoldItem.putIfAbsent(item.getName(), item.GetComputedSubtotal());
        }

        assertEquals(5, tempSoldItem.size());
    }

    @Test
    public void soldItemsTestDateRange() {
//        SoldItem(String name, String category, int quantity, double productPrice, String created_at, String created_time)
        ArrayList<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(new SoldItem("Sprite", "Softdrinks", 2, 20, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Tapsilog", "Food", 2, 45, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Nova", "Snacks", 2, 19, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11", "06:37 PM"));

        soldItems.add(new SoldItem("Coke", "Softdrinks", 3, 15, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 2, 15, "2022-12-11", "06:37 PM"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11", "06:37 PM"));

//        soldItems.a

//        Map<String, Double> tempSoldItem = new HashMap<>();
//        for (SoldItem item : soldItems) {
//            tempSoldItem.computeIfPresent(item.getName(), (s, v) -> v + item.GetComputedSubtotal());
//            tempSoldItem.putIfAbsent(item.getName(), item.GetComputedSubtotal());
//        }

        assertEquals(5, tempSoldItem.size());
    }


}

package com.example.inventorymanagementsystem;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class SalesFormTest {

    @Test
    public void soldItemsTestByName() {
//        SoldItem(String name, String category, int quantity, double productPrice, String created_at, String created_time)
        ArrayList<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(new SoldItem("Sprite", "Softdrinks", 2, 20, "2022-12-11"));
        soldItems.add(new SoldItem("Tapsilog", "Food", 2, 45, "2022-12-11"));
        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11"));

        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11"));

        soldItems.add(new SoldItem("Nova", "Snacks", 2, 19, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11"));

        soldItems.add(new SoldItem("Coke", "Softdrinks", 3, 15, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 2, 15, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11"));

        Map<String, Double> tempSoldItem = new HashMap<>();
        for (SoldItem item : soldItems) {
            tempSoldItem.computeIfPresent(item.getName(), (s, v) -> v + item.GetComputedSubtotal());
            tempSoldItem.putIfAbsent(item.getName(), item.GetComputedSubtotal());
        }

        assertEquals(5, tempSoldItem.size());
    }

    @Test
    public void soldItemsTestByCategory() {
//        SoldItem(String name, String category, int quantity, double productPrice, String created_at, String created_time)
        ArrayList<SoldItem> soldItems = new ArrayList<>();
        soldItems.add(new SoldItem("Sprite", "Softdrinks", 2, 20, "2022-12-11"));
        soldItems.add(new SoldItem("Tapsilog", "Food", 2, 45, "2022-12-11"));
        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11"));

        soldItems.add(new SoldItem("Happy", "Snacks", 5, 10, "2022-12-11"));

        soldItems.add(new SoldItem("Nova", "Snacks", 2, 19, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11"));

        soldItems.add(new SoldItem("Coke", "Softdrinks", 3, 15, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 2, 15, "2022-12-11"));
        soldItems.add(new SoldItem("Coke", "Softdrinks", 1, 15, "2022-12-11"));

        Map<String, Double> tempSoldItem = new HashMap<>();
        for (SoldItem item : soldItems) {
            tempSoldItem.computeIfPresent(item.getName(), (s, v) -> v + item.GetComputedSubtotal());
            tempSoldItem.putIfAbsent(item.getName(), item.GetComputedSubtotal());
        }

        assertEquals(5, tempSoldItem.size());
    }

    @Test
    public void SalesByDateRangeYearTest() {
// Sales(int quantity, double amountPayable, double amountReceived, double amountChange, String created_at)
        //Data
        ArrayList<Sales> salesItemList = new ArrayList<>();
        salesItemList.add(new Sales(2,100, 1000, 508,"2023-11-09"));
        salesItemList.add(new Sales(3,50, 1000, 508,"2022-10-09"));
        salesItemList.add(new Sales(4,80, 1000, 508,"2021-08-09"));
        salesItemList.add(new Sales(5,72, 1000, 508,"2020-07-09"));
        salesItemList.add(new Sales(6,80, 1000, 508,"2019-06-09"));

        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
        Map<String, Double> tempSaleItemMap = new HashMap<>();
        for (Sales item : salesItemList) {
            LocalDate tempDate = LocalDate.parse(item.getCreated_at());
            String month = dateTimeDateShort.format(tempDate);

            tempSaleItemMap.computeIfPresent(month, (s, v) -> v + item.getAmountPayable());
            tempSaleItemMap.putIfAbsent(month, item.getAmountPayable());
        }

        assertEquals(5, tempSaleItemMap.size());

    }

    @Test
    public void SalesByDateRangeMonthTest() {
// Sales(int quantity, double amountPayable, double amountReceived, double amountChange, String created_at)
        //Data
        ArrayList<Sales> salesItemList = new ArrayList<>();
        salesItemList.add(new Sales(2,100, 1000, 508,"2022-11-09"));
        salesItemList.add(new Sales(3,50, 1000, 508,"2022-10-09"));
        salesItemList.add(new Sales(4,80, 1000, 508,"2022-08-09"));
        salesItemList.add(new Sales(5,72, 1000, 508,"2022-07-09"));
        salesItemList.add(new Sales(6,80, 1000, 508,"2022-06-09"));

        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("MMMyyyy", Locale.ENGLISH);

        Map<String, Double> tempSaleItemMap = new HashMap<>();
        for (Sales item : salesItemList) {
            LocalDate tempDate = LocalDate.parse(item.getCreated_at());
            String month = dateTimeDateShort.format(tempDate);

            tempSaleItemMap.computeIfPresent(month, (s, v) -> v + item.getAmountPayable());
            tempSaleItemMap.putIfAbsent(month, item.getAmountPayable());
        }

        assertEquals(5, tempSaleItemMap.size());

    }

 @Test
    public void SalesByDateRangeWeekTest() {
// Sales(int quantity, double amountPayable, double amountReceived, double amountChange, String created_at)

     //Data
        ArrayList<Sales> salesItemList = new ArrayList<>();
        salesItemList.add(new Sales(2,100, 1000, 508,"2022-11-09"));
        salesItemList.add(new Sales(3,50, 1000, 508,"2022-12-09"));
        salesItemList.add(new Sales(4,80, 1000, 508,"2022-12-08"));
        salesItemList.add(new Sales(5,72, 1000, 508,"2022-12-07"));
        salesItemList.add(new Sales(6,80, 1000, 508,"2022-12-06"));

        salesItemList.add(new Sales(6,10, 10, 0,"2022-12-06"));
        salesItemList.add(new Sales(6,200, 200, 0,"2022-12-06"));
        salesItemList.add(new Sales(6,3000, 3000, 0,"2022-12-06"));

        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("MMM-dd-yy", Locale.ENGLISH);

        Map<String, Double> tempSaleItemMap = new HashMap<>();
        for (Sales item : salesItemList) {
            LocalDate tempDate = LocalDate.parse(item.getCreated_at());
            String month = dateTimeDateShort.format(tempDate);

            tempSaleItemMap.computeIfPresent(month, (s, v) -> v + item.getAmountPayable());
            tempSaleItemMap.putIfAbsent(month, item.getAmountPayable());
        }

        assertEquals(5, tempSaleItemMap.size());

    }


}

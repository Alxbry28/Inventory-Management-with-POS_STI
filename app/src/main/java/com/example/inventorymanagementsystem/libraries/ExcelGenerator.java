package com.example.inventorymanagementsystem.libraries;

import android.content.Context;
import android.os.Environment;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;
import com.example.inventorymanagementsystem.models.SoldItemReport;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExcelGenerator {

    private HSSFWorkbook hssfWorkbook;
    private Context context;
    private ArrayList<Sales> tempSalesList;
    private ArrayList<SoldItemReport> tempSoldItemList;
    private String businessName, startDate, endDate, dateGenerated;
    private String fileNameUnique;

    public ExcelGenerator(Context context) {

        this.context = context;
        hssfWorkbook = new HSSFWorkbook();

    }

    public boolean generateSales() {

        SalesWorkbook();
        SoldProductWorkbook();

        File filePath = new File(context.getFilesDir(), Sales.FILENAME);

//  File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Sales.FILENAME);

        try {
            if (filePath.exists()){
                filePath.delete();

                if (!filePath.exists()){
                    filePath.createNewFile();
                }

            }
            else{
                filePath.createNewFile();
            }

//            context.openFileOutput()
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            return filePath.exists();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void SalesWorkbook(){
//        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sales Report");
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRowHeader = hssfSheet.createRow(0);
        HSSFCell hssfCellHeader  = hssfRowHeader.createCell(2);
        hssfCellHeader.setCellValue("Sales Report");

        HSSFRow hssfRowDateRange = hssfSheet.createRow(1);
        HSSFCell hssfCellDateFrom  = hssfRowDateRange.createCell(0);
        hssfCellDateFrom.setCellValue("From: " + startDate);

        HSSFCell hssfCellDateTo  = hssfRowDateRange.createCell(3);
        hssfCellDateTo.setCellValue("To: " + endDate);

        String[] salesHeader = new String[]{"Receipt No", "Amount Payable", "Item Qty", "Date"};
        HSSFRow hssfRowDataHeader = hssfSheet.createRow(2);
        for (int i = 0; i < salesHeader.length; i++) {
            HSSFCell hssfCellDataHeader  = hssfRowDataHeader.createCell(i);
            hssfCellDataHeader.setCellValue(salesHeader[i]);
        }

        int beginRow = 3;
        int totalQty = 0;
        double totalSold = 0;
        HSSFCell hssfCellData;
        for (Sales salesItem : tempSalesList) {
            HSSFRow hssfRowData = hssfSheet.createRow(beginRow);

            hssfCellData = hssfRowData.createCell(0); //"Receipt No"
            hssfCellData.setCellValue(salesItem.getReceiptNo());

            hssfCellData = hssfRowData.createCell(1); //"Amount Payable"
            hssfCellData.setCellValue(MoneyLibrary.toTwoDecimalPlaces(salesItem.getAmountPayable()));

            hssfCellData = hssfRowData.createCell(2); //"Item Qty"
            hssfCellData.setCellValue(salesItem.getQuantity());

            hssfCellData = hssfRowData.createCell(3); //"Date"
            hssfCellData.setCellValue(salesItem.getCreated_at());

            totalQty += salesItem.getQuantity();
            totalSold += salesItem.getAmountPayable();
            beginRow++;
        }

        HSSFRow hssfRowDataBusinessName = hssfSheet.createRow(beginRow + 1);
        hssfCellData = hssfRowDataBusinessName.createCell(0); // "businessName"
        hssfCellData.setCellValue("Business Name: " +  businessName);

        HSSFRow hssfRowDataTotal = hssfSheet.createRow(beginRow + 2);
        hssfCellData = hssfRowDataTotal.createCell(0); // "Total"
        hssfCellData.setCellValue("Total: " + MoneyLibrary.toTwoDecimalPlaces(totalSold));

        hssfRowDataTotal = hssfSheet.createRow(beginRow + 3);
        hssfCellData = hssfRowDataTotal.createCell(0); // "Quantity"
        hssfCellData.setCellValue("Total Qty: " + totalQty);

        HSSFRow hssfRowGenDate = hssfSheet.createRow(beginRow + 4);
        hssfCellData = hssfRowGenDate.createCell(0); // "Date Generated"
        hssfCellData.setCellValue("Date Generated: " + dateGenerated);

    }

    private void SoldProductWorkbook(){
//        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sold Product Report");
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRowHeader = hssfSheet.createRow(0);
        HSSFCell hssfCellHeader  = hssfRowHeader.createCell(3);
        hssfCellHeader.setCellValue("Sold Product Report");

        HSSFRow hssfRowDateRange = hssfSheet.createRow(1);
        HSSFCell hssfCellDateFrom  = hssfRowDateRange.createCell(0);
        hssfCellDateFrom.setCellValue("From: " + startDate);

        HSSFCell hssfCellDateTo  = hssfRowDateRange.createCell(3);
        hssfCellDateTo.setCellValue("To: " + endDate);

        String[] header = new String[]{"Receipt No", "Product Name", "Category", "SRP", "Quantity", "Total", "Date"};
        HSSFRow hssfRowDataHeader = hssfSheet.createRow(2);
        for (int i = 0; i < header.length; i++) {
            HSSFCell hssfCellDataHeader  = hssfRowDataHeader.createCell(i);
            hssfCellDataHeader.setCellValue(header[i]);
        }

        int beginRow = 3;
        int totalQty = 0;
        double totalSold = 0;
        HSSFCell hssfCellData;
        for (SoldItemReport soldItem : tempSoldItemList) {
            HSSFRow hssfRowData = hssfSheet.createRow(beginRow);

            hssfCellData = hssfRowData.createCell(0); //"Receipt No"
            hssfCellData.setCellValue(soldItem.getReceiptNo());

            hssfCellData = hssfRowData.createCell(1); //"Product Name"
            hssfCellData.setCellValue(soldItem.getName());

            hssfCellData = hssfRowData.createCell(2); //"Category"
            hssfCellData.setCellValue(soldItem.getCategory());

            hssfCellData = hssfRowData.createCell(3); //"SRP"
            hssfCellData.setCellValue(soldItem.getProductPrice());

            hssfCellData = hssfRowData.createCell(4); // "Quantity"
            hssfCellData.setCellValue(soldItem.getQuantity());

            hssfCellData = hssfRowData.createCell(5); // "Total"
            hssfCellData.setCellValue(MoneyLibrary.toTwoDecimalPlaces(soldItem.GetComputedSubtotal()));

            hssfCellData = hssfRowData.createCell(6); // Date
            hssfCellData.setCellValue(soldItem.getCreated_at());

            totalQty += soldItem.getQuantity();
            totalSold += soldItem.GetComputedSubtotal();
            beginRow++;
        }

        HSSFRow hssfRowDataBusinessName = hssfSheet.createRow(beginRow + 1);
        hssfCellData = hssfRowDataBusinessName.createCell(0); // "businessName"
        hssfCellData.setCellValue("Business Name: " +  businessName);

        HSSFRow hssfRowDataTotal = hssfSheet.createRow(beginRow + 2);
        hssfCellData = hssfRowDataTotal.createCell(0); // "Total"
        hssfCellData.setCellValue("Total Sold: " + MoneyLibrary.toTwoDecimalPlaces(totalSold));

        hssfRowDataTotal = hssfSheet.createRow(beginRow + 3);
        hssfCellData = hssfRowDataTotal.createCell(0); // "Quantity"
        hssfCellData.setCellValue("Total Qty: " +totalQty);

        HSSFRow hssfRowGenDate = hssfSheet.createRow(beginRow + 4);
        hssfCellData = hssfRowGenDate.createCell(0); // "Date Generated"
        hssfCellData.setCellValue("Date Generated: " + dateGenerated);

    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public ArrayList<Sales> getTempSalesList() {
        return tempSalesList;
    }

    public void setTempSalesList(ArrayList<Sales> tempSalesList) {
        this.tempSalesList = tempSalesList;
    }

    public ArrayList<SoldItemReport> getTempSoldItemList() {
        return tempSoldItemList;
    }

    public void setTempSoldItemList(ArrayList<SoldItemReport> tempSoldItemList) {
        this.tempSoldItemList = tempSoldItemList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }
}

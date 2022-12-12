package com.example.inventorymanagementsystem.libraries;

import android.content.Context;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelGenerator {

    private HSSFWorkbook hssfWorkbook;
//    private File filePath;
    private Context context;
    public ExcelGenerator(Context context) {
        this.context = context;
        hssfWorkbook = new HSSFWorkbook();
    }

    public boolean testGenerate() {
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("Testing Geneation of Excel");

        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "testing.xlsx");

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

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

    private boolean generateSales() {
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("Generated Sales");

        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sales.xlsx");

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

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
}

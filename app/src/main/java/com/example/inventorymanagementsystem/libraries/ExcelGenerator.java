package com.example.inventorymanagementsystem.libraries;

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
    private File filePath;

    public ExcelGenerator() {
        hssfWorkbook = new HSSFWorkbook();
        filePath = new File(Environment.getExternalStorageDirectory() + "/POS_INVENTORY/test.xls");

    }

    private boolean testGenerate() {

        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("Testing Geneation of Excel");

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

    private void generateSales() {


    }
}

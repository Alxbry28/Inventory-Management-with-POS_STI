package com.example.inventorymanagementsystem.libraries;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyLibrary {
    public static String toTwoDecimalPlaces(double value){
        double roundOff = Math.round(value * 100.0) / 100.0;
        DecimalFormat df = new DecimalFormat("0.00");
//        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(roundOff);
    }
}

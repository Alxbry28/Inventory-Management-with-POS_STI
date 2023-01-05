package com.example.inventorymanagementsystem.libraries;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Validation {

    public static boolean isValueDouble(String value){
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean checkPasswordMatch(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }


}

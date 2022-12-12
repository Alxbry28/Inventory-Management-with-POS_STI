package com.example.inventorymanagementsystem.enums;

public enum MailServiceEnum {
    HOST("smtp.gmail.com"),
    PORT("465"),
    SSL( "true"),
    AUTH( "true");
    String Value;
    MailServiceEnum(String value) {
        Value = value;
    }
}

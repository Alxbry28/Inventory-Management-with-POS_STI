package com.example.inventorymanagementsystem.enums;

public enum StaffPosition{
    EMPLOYEE(3,"Employee"),
    BUSINESSOWNER(2,"Business Owner"),
    ADMIN(1, "Administrator"),
    SUPERADMIN(4,"Super Administrator");

    int userType;
    String position;
    StaffPosition(int UserType, String Position){
        userType = UserType;
        position = Position;
    }

}

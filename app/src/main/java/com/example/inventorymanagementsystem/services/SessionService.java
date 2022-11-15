package com.example.inventorymanagementsystem.services;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;

public class SessionService {

    private SharedPreferences mySharedPref;
    private Editor mySharedPrefEditor;
    private Staff staff;
    private String businessName;

    public boolean sessionStaff(){
        mySharedPrefEditor = mySharedPref.edit();
        mySharedPrefEditor.putBoolean("isSignIn", true);
        mySharedPrefEditor.putString("userId", staff.getUserId());
        mySharedPrefEditor.putString("storeId", staff.getStoreId());
        mySharedPrefEditor.putString("staffId", staff.getId());
        mySharedPrefEditor.putString("businessName", businessName);
        mySharedPrefEditor.apply();
        return mySharedPrefEditor.commit();
    }

    public boolean sessionUser(){
        mySharedPrefEditor = mySharedPref.edit();
        mySharedPrefEditor.putBoolean("isSignIn", true);
        mySharedPrefEditor.putString("userId", staff.getId());
        mySharedPrefEditor.apply();
        return mySharedPrefEditor.commit();
    }

    public boolean Destroy(){
        mySharedPrefEditor = mySharedPref.edit();
        mySharedPrefEditor.clear();
        mySharedPrefEditor.apply();
        return mySharedPrefEditor.commit();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public SharedPreferences getMySharedPref() {
        return mySharedPref;
    }

    public void setMySharedPref(SharedPreferences mySharedPref) {
        this.mySharedPref = mySharedPref;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}

package com.example.inventorymanagementsystem.services;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;

public class SessionService {

    private SharedPreferences mySharedPref;
    private Editor mySharedPrefEditor;
    private String userId;
    private User user;


    public boolean BeginStaff(){
        mySharedPrefEditor = mySharedPref.edit();
        mySharedPrefEditor.putBoolean("isSignIn", true);
        mySharedPrefEditor.putString("userId", user.getId());
        mySharedPrefEditor.putString("storeId", user.getStoreId());
        mySharedPrefEditor.apply();
        return mySharedPrefEditor.commit();
    }

    public boolean End(){
        mySharedPrefEditor = mySharedPref.edit();
        mySharedPrefEditor.clear();
        mySharedPrefEditor.apply();
        return mySharedPrefEditor.commit();
    }

    public SharedPreferences getMySharedPref() {
        return mySharedPref;
    }

    public void setMySharedPref(SharedPreferences mySharedPref) {
        this.mySharedPref = mySharedPref;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

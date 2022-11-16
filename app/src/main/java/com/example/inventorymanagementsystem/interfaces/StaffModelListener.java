package com.example.inventorymanagementsystem.interfaces;

import com.example.inventorymanagementsystem.models.Staff;
import java.util.ArrayList;

public interface StaffModelListener {
    void retrieveStaff(Staff staff);
    void getStaffList(ArrayList<Staff> staffList);
}

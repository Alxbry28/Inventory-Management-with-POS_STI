package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.adapters.StaffRCVAdapter;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.models.Staff;

import java.util.ArrayList;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
public class StaffListActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private RecyclerView rcStaffList;
    private Button btnAddStaff, btnBack;
    private TextView tvEmptyStaffMsg;
    private Staff staff;
    private ArrayList<Staff> staffArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
//        getSupportActionBar().hide();
        staff = new Staff();

        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        rcStaffList = findViewById(R.id.rcStaffList);
        tvEmptyStaffMsg = findViewById(R.id.tvEmptyStaffMsg);

        btnAddStaff = findViewById(R.id.btnAddStaff);
        btnBack = findViewById(R.id.btnBackToHome_staff);

        staff.setStoreId(storeId);
        staff.GetAll(new StaffModelListener() {
            @Override
            public void retrieveStaff(Staff staff) {

            }

            @Override
            public void getStaffList(ArrayList<Staff> staffList) {
                if(!staffList.isEmpty()){
                    tvEmptyStaffMsg.setVisibility(View.INVISIBLE);
                    rcStaffList.setVisibility(View.VISIBLE);

                    StaffRCVAdapter staffRCVAdapter = new StaffRCVAdapter();
                    staffRCVAdapter.setContext(StaffListActivity.this);
                    staffRCVAdapter.setStaffArrayList(staffList);
                    staffRCVAdapter.setCurrentUserId(userId);

                    rcStaffList.setAdapter(staffRCVAdapter);
                    RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(StaffListActivity.this);
                    rcStaffList.setLayoutManager(rcvLayoutManager);
                    rcStaffList.setItemAnimator(new DefaultItemAnimator());

                }
            }
        });


        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffListActivity.this, AddEditStaffActivity.class));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
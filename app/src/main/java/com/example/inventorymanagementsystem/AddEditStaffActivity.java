package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class AddEditStaffActivity extends AppCompatActivity {

    private Button btnAddEditStaff, btnBackStaffList;
    private TextView tvBusinessName;
    private FirebaseAuth firebaseAuth;
    private User user;
    private Staff staff;
    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId, staffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_staff);

        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);
        user = new User();

        tvBusinessName = findViewById(R.id.tvBusinessName);
        tvBusinessName.setText(businessName);


        btnAddEditStaff = findViewById(R.id.btnAddEditStaff);

        btnAddEditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEditStaffActivity.this, "Test add", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackStaffList = findViewById(R.id.btnBackToStaff);

                btnBackStaffList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

    }
}
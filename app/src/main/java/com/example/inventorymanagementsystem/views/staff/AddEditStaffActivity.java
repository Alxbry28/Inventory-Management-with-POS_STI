package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.dialogs.PositionChoiceDialog;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.example.inventorymanagementsystem.libraries.Validation;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddEditStaffActivity extends AppCompatActivity {

    private Button btnAddEditStaff, btnBackStaffList;
    private TextView tvBusinessName,tvStaffTransactionType;
    private EditText etRole, etFirstname,etLastname, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth firebaseAuth;

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId, staffId;
    private boolean isEdit = false;
    private String editUserId, editStaffId;
    private PositionChoiceDialog positionChoiceDialog;
    private FirebaseAuth mAuth;
    private User user;
    private Staff staff;
    private LinearLayout lnrPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_staff);
//        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        user = new User();
        user.setStoreId(storeId);

        staff = new Staff();
        staff.setStoreId(storeId);

        mAuth = FirebaseAuth.getInstance();

        initComponent();
        initDialog();

        tvBusinessName.setText(businessName);

        isEdit = getIntent().getBooleanExtra("isEditStaff", false);
        if(isEdit){
            lnrPasswords = findViewById(R.id.lnrPasswords);
            lnrPasswords.setVisibility(
                    View.INVISIBLE
            );
            editUserId = getIntent().getStringExtra("userId");
            editStaffId = getIntent().getStringExtra("staffId");

            user.setId(editUserId);
            user.GetById(new UserModelListener() {
                @Override
                public void retrieveUser(User user1) {
                    user = user1;
                    etEmail.setText(user1.getEmail());
                    etEmail.setEnabled(false);
                }
            });

            staff.setId(editStaffId);
            staff.GetById(new StaffModelListener() {
                @Override
                public void retrieveStaff(Staff staff1) {
                    staff = staff1;
                    etFirstname.setText(staff1.getFirstname());
                    etLastname.setText(staff1.getLastname());
                    etRole.setText(staff1.getPosition());
                }

                @Override
                public void getStaffList(ArrayList<Staff> staffList) {

                }
            });

            if(etRole.getText().toString().equals("Business Owner")) etRole.setEnabled(false);
            btnAddEditStaff.setText("Save");
            tvStaffTransactionType.setText("Edit Staff");
        }


        initComponentEvents();
    }

    private void initDialog(){
        positionChoiceDialog = new PositionChoiceDialog();
        positionChoiceDialog.setContext(AddEditStaffActivity.this);
    }

    private void initComponent(){
        btnAddEditStaff = findViewById(R.id.btnAddEditStaff);
        btnBackStaffList = findViewById(R.id.btnBackToStaff);

        tvStaffTransactionType = findViewById(R.id.tvStaffTransactionType);
        tvBusinessName = findViewById(R.id.tvBusinessName);
        etEmail = findViewById(R.id.etEmail);

        etRole = findViewById(R.id.etRole);
        etRole.setFocusable(false);

        etFirstname = findViewById(R.id.etFirstName);
        etLastname = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

    }

    private void initComponentEvents(){
        etRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etRole.getText().toString().equals("Business Owner")) {
                    positionChoiceDialog.setChosenPosition(etRole.getText().toString());
                    positionChoiceDialog.show(getSupportFragmentManager(),"dialog_choose_position");
                    positionChoiceDialog.getPositionChoice(new PositionChoiceDialog.PositionChoiceListener() {
                        @Override
                        public void setPositionChoice(String choice) {
                            etRole.setText(choice);
                        }
                    });
                }
                else{
                    Toast.makeText(AddEditStaffActivity.this, "Unable to edit this field, you are the business owner.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnAddEditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etFirstname.getText().toString()) || TextUtils.isEmpty(etLastname.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty((etRole.getText().toString()))){
                    Toast.makeText(AddEditStaffActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
                    etEmail.setError("Please provide valid email!");
                    etEmail.requestFocus();
                    return;
                }

                user.setStoreId(storeId);
                staff.setFirstname(etFirstname.getText().toString());
                staff.setLastname(etLastname.getText().toString());
                staff.setPosition(etRole.getText().toString());
                user.setEmail(etEmail.getText().toString());


                isEdit = getIntent().getBooleanExtra("isEditStaff", false);

                switch(etRole.getText().toString()){
                    case "Administrator":
                        user.setUserType(1);
                        break;
                    case "Staff":
                        user.setUserType(3);
                        break;
                }

                if(isEdit){
                    staff.Update(new TransactionStatusListener() {
                        @Override
                        public void checkStatus(boolean status) {
                            if(status){

                                user.Update(new TransactionStatusListener() {
                                    @Override
                                    public void checkStatus(boolean status) {
                                      Toast.makeText(AddEditStaffActivity.this, "Staff Edit Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddEditStaffActivity.this, StaffListActivity.class));
                                        finish();
                                    }
                                });
                            }
                        }
                    });


                }
                else{

                    if(!Validation.checkPasswordMatch(etPassword.getText().toString(),etConfirmPassword.getText().toString())){
                        Toast.makeText(AddEditStaffActivity.this, "Password don't match.", Toast.LENGTH_SHORT).show();
                      return;
                    }

                    if(TextUtils.isEmpty(etFirstname.getText().toString()) || TextUtils.isEmpty(etLastname.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString()) || TextUtils.isEmpty((etConfirmPassword.getText().toString()))){
                        Toast.makeText(AddEditStaffActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    user.setPassword(etPassword.getText().toString());
                    mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    user.setId(userId);



                                    staff.setUserId(userId);
                                    user.setStoreId(storeId);
                                    staff.setStoreId(storeId);
                                    user.Create(new TransactionStatusListener() {
                                        @Override
                                        public void checkStatus(boolean status) {
                                            if(status == true){
                                                staff.Create(new TransactionStatusListener() {
                                                    @Override
                                                    public void checkStatus(boolean status) {
                                                        if(status == true){
                                                            Toast.makeText(AddEditStaffActivity.this,"Your registration is Success. Check your email to verify your account.",Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(AddEditStaffActivity.this, StaffListActivity.class));
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(AddEditStaffActivity.this,"Failed to register. Check your connection or try different email.",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        btnBackStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddEditStaffActivity.this, StaffListActivity.class));
                finish();
            }
        });

    }

}
package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.models.BusinessOwner;
import com.example.inventorymanagementsystem.models.Store;
import com.example.inventorymanagementsystem.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private TextView banner;
    private Button registerUser, btnBack;
    private TextInputLayout etbname, etconfirmPass, etEmail, etPassword, etFirstname, etLastname;
    private ProgressBar progressBar;
    private User userRegister;
    private Store store;
    private BusinessOwner staff;
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
//        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etFirstname = findViewById(R.id.firstname);
        etLastname = findViewById(R.id.lastname);
        etbname = findViewById(R.id.busName);
        etconfirmPass = findViewById(R.id.confirmPass);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
            break;
            case R.id.registerUser:
                registerUser();
            break;
        }
    }

    private void registerUser(){
        staff = new BusinessOwner();
        staff.setFirstname(etFirstname.getEditText().getText().toString().trim());
        staff.setLastname(etLastname.getEditText().getText().toString().trim());

        userRegister = new User();
        userRegister.setPassword(etPassword.getEditText().getText().toString().trim());
        userRegister.setEmail(etEmail.getEditText().getText().toString().trim());

        store = new Store();
        store.setName(etbname.getEditText().getText().toString().trim());

        String confirmPass = etconfirmPass.getEditText().getText().toString().trim();

    if (store.getName().isEmpty()){
        etbname.setError("Business name required!");
        etbname.requestFocus();
        return;
    }
    if (userRegister.getEmail().isEmpty()){
        etEmail.setError("Email required!");
        etEmail.requestFocus();
        return;
    }
    if (userRegister.getPassword().isEmpty()){
        etPassword.setError("Password required!");
        etPassword.requestFocus();
        return;
    }
    if (confirmPass.isEmpty()){
        etconfirmPass.setError("Please confirm password");
        etconfirmPass.requestFocus();
        return;
    }

    if(!Patterns.EMAIL_ADDRESS.matcher(userRegister.getEmail()).matches()){
        etEmail.setError("Please provide valid email!");
        etEmail.requestFocus();
        return;
    }

    if (userRegister.getPassword().isEmpty()){
        etPassword.setError("Password Required");
        etPassword.requestFocus();
        return;
    }
    if (userRegister.getPassword().length() < 8)
    {
        etPassword.setError("Password should be minimum of 8!");
        etPassword.requestFocus();
        return;
    }

    if (PASSWORD_PATTERN.matcher(userRegister.getPassword()).matches())
    {
        etPassword.setError("Password must at least 1 upper case, at least 1 lower, at least 1 special charcter");
        etPassword.requestFocus();
        return;
    }

    if (!userRegister.getPassword().equals(confirmPass))
    {
        etconfirmPass.setError("Password dont match.");
        etconfirmPass.requestFocus();
        return;
    }

    progressBar.setVisibility(View.VISIBLE);
    mAuth.createUserWithEmailAndPassword(userRegister.getEmail(),userRegister.getPassword())
            .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        userRegister.setId(userId);
                        userRegister.setUserType(2);
                        staff.setUserId(userId);
                        store.setUserId(userId);
                        store.setOwner(staff.getFullname());

                        store.Create(new TransactionStatusListener() {
                            @Override
                            public void checkStatus(boolean status) {
                                if(status == true){
                                    userRegister.setStoreId(store.getId());
                                    staff.setStoreId(store.getId());
                                    userRegister.Create(new TransactionStatusListener() {
                                        @Override
                                        public void checkStatus(boolean status) {
                                            if(status == true){
                                                staff.Create(new TransactionStatusListener() {
                                                    @Override
                                                    public void checkStatus(boolean status) {
                                                        if(status == true){
                                                            Toast.makeText(RegisterUser.this,"Your registration is Success. Check your email to verify your account.",Toast.LENGTH_LONG).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(RegisterUser.this,"Failed to register. Check your connection or try different email.",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
            });

    }
    private String time ()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
    private String date ()
    {
        return new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(new Date());
    }
}
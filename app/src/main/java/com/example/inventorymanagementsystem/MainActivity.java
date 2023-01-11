package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.inventorymanagementsystem.views.staff.*;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.StoreModelListener;
import com.example.inventorymanagementsystem.interfaces.UserModelListener;
import com.example.inventorymanagementsystem.models.BusinessOwner;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.models.Store;
import com.example.inventorymanagementsystem.services.AuthService;
import com.example.inventorymanagementsystem.services.SessionService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private TextInputLayout etEmail, etPassword;
    private Button LogIn, btnClose;

    private AuthService authService;
    private ProgressBar progressBar;
    private User logUser;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    private SharedPreferences sharedPreferences;
    private SessionService sessionService;

    public static final String TAG = "com.example.inventorymanagementsystem.mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logUser = new User();
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        boolean isSignIn = sharedPreferences.getBoolean("isSignIn", false);
        if(isSignIn){
            int userType = sharedPreferences.getInt("userType", 0);
            redirectUser(userType);
            finish();
        }

        sessionService = new SessionService();
        sessionService.setMySharedPref(sharedPreferences);

        authService = new AuthService();
        mAuth = authService.getFirebaseAuth();

        register = (TextView)findViewById(R.id.register);
        register.setPaintFlags(register.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        register.setOnClickListener(this);

        LogIn = (Button)findViewById(R.id.signIn);
        LogIn.setOnClickListener(this);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener((View v) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Close");
            builder.setMessage("Do you want to close this app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    dialog.dismiss();
                    finishAffinity();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

        etEmail = (TextInputLayout) findViewById(R.id.email);
        etPassword = (TextInputLayout)findViewById(R.id.password);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:;
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin(){

        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Email required!");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etPassword.setError("Please enter your password.");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etPassword.setError("Password should be minimum of 6!");
            etPassword.requestFocus();
            return;
        }

        if (RegisterUser.PASSWORD_PATTERN.matcher(password).matches())
        {
            etPassword.setError("Password must at least 1 upper case, at least 1 lower, at least 1 special charcter");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        logUser.setEmail(email);
        logUser.setPassword(password);

//        authService.setUser(user);
//        AuthResult authResult = authService.Login();
//        Toast.makeText(this, "loginResult: " + authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(logUser.getEmail(),logUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Redirect to activity after successful login
                    FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = firebaseUser.getUid();
                        logUser.setId(userID);
                        logUser.GetById(userExist -> {
                                if(userExist != null){
                                    sessionService.setUser(userExist);
                                    if(sessionService.BeginStaff()){
                                        Toast.makeText(MainActivity.this,"Successfully Login",Toast.LENGTH_LONG).show();
                                        redirectUser(userExist.getUserType());
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            }
                    );

                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Please check your email to verify your account.",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"login Failed! Please check your credentials!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void redirectUser(int userType){
        switch (userType){
            case 1:

                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;
            case 3:

                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                break;
        }
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
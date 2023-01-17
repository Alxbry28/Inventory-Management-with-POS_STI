package com.example.inventorymanagementsystem.views.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.dialogs.POSProductSelectionDialog;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.StoreModelListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.Store;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.services.SessionService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.inventorymanagementsystem.views.staff.*;
import com.example.inventorymanagementsystem.models.Sales;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
import androidx.activity.OnBackPressedCallback;
import android.content.DialogInterface;

public class HomeActivity extends AppCompatActivity {

    private String userId, storeId, staffId, businessName;
    private int userType;
    private Boolean isSignIn;
    private TextView tvBusinessName;
    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private Button Logout, Items,Sales,Transactions,Inventory, btnPOS, btnStaff;
    private Button btnTest;
    private View.OnClickListener menuButtonClickListener;
    private SessionService sessionService;
    private CartLibrary cartLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        getSupportActionBar().hide();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       logout();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sessionService = new SessionService();
        sharedPreferences = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);

        File filePath = new File(getFilesDir(), com.example.inventorymanagementsystem.models.Sales.FILENAME);
        String path = this.getFilesDir().getAbsolutePath();

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        cartLibrary = new CartLibrary();
        cartLibrary.setSqLiteDB(new SQLiteDB(HomeActivity.this));

        userId = sharedPreferences.getString("userId","");
        storeId = sharedPreferences.getString("storeId","");
        userType = sharedPreferences.getInt("userType",-1);
        isSignIn = sharedPreferences.getBoolean("isSignIn",false);
        sessionService.setMySharedPref(sharedPreferences);

        tvBusinessName = findViewById(R.id.tvbusName);
        if(businessName == null){
            Store store = new Store();
            store.setId(storeId);
            store.GetById(storeExist ->{
                SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
                sharedEditor.putString("businessName", storeExist.getName());
                sharedEditor.apply();
                sharedEditor.commit();
                businessName = storeExist.getName();
                tvBusinessName.setText(businessName);
            });
        }

        initButtons();
        if (userType == 3){
            Sales.setVisibility(View.GONE);
            btnStaff.setVisibility(View.GONE);
        }

        businessName = sharedPreferences.getString("businessName",null);
        tvBusinessName.setText(businessName);

        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

        cartLibrary.clear();
    }

    private void logout(){
        // Back is pressed... Finishing the activity
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionService.End();
                dialog.dismiss();
                dialog.cancel();
                cartLibrary.clear();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initButtons(){
        btnPOS = findViewById(R.id.btnPOS);
        btnPOS.setOnClickListener(getMenuButtonClickListener());

        Inventory = (Button)findViewById(R.id.btnInventory);
        Inventory.setOnClickListener(getMenuButtonClickListener());

        Logout = (Button)findViewById(R.id.btnlogout);
        Logout.setOnClickListener(getMenuButtonClickListener());

        Sales = (Button)findViewById(R.id.btnSales);
        Sales.setOnClickListener(getMenuButtonClickListener());

        Transactions = (Button)findViewById(R.id.btnTransactions);
        Transactions.setOnClickListener(getMenuButtonClickListener());

        Items = (Button)findViewById(R.id.btnItems);
        Items.setOnClickListener(getMenuButtonClickListener());

        btnStaff = findViewById(R.id.btnStaff);
        btnStaff.setOnClickListener(getMenuButtonClickListener());
    }

    public View.OnClickListener getMenuButtonClickListener() {
        return v -> {
                switch (v.getId()){
                    case R.id.btnItems:
                        startActivity(new Intent(HomeActivity.this, ItemsForm.class));
                        break;

                    case R.id.btnTransactions:
                        startActivity(new Intent(HomeActivity.this, TransactionsForm.class));
                        break;

                    case R.id.btnSales:
                        startActivity(new Intent(HomeActivity.this,SalesForm.class));
                        break;

                    case R.id.btnInventory:
                        startActivity(new Intent(HomeActivity.this, InventoryForm.class));
                        break;

                    case R.id.btnPOS:
                        startActivity(new Intent(HomeActivity.this, POSItemActivity.class));
                        break;

                    case R.id.btnStaff:
                        startActivity(new Intent(HomeActivity.this, StaffListActivity.class));
                        break;

                    default:
                            logout();
                }
        };
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
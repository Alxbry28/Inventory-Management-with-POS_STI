package com.example.inventorymanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.dialogs.POSProductSelectionDialog;
import com.example.inventorymanagementsystem.interfaces.StaffModelListener;
import com.example.inventorymanagementsystem.interfaces.StoreModelListener;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private String userId, storeId, staffId, businessName;
    private Boolean isSignIn;
    private TextView tvBusinessName;
    private SharedPreferences sharedPreferences;
    private Button Logout, Items,Sales,Transactions,Inventory, btnPOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        businessName = getIntent().getStringExtra("businessName");
        tvBusinessName = findViewById(R.id.tvbusName);
//        tvBusinessName.setText(businessName);

        sharedPreferences = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        isSignIn = sharedPreferences.getBoolean("isSignIn",false);

        Toast.makeText(this, "userId: " + userId, Toast.LENGTH_SHORT).show();

        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

        btnPOS = findViewById(R.id.btnPOS);
        btnPOS.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,POSActivity.class))    ;
//            Toast.makeText(this, "Test POS Button", Toast.LENGTH_SHORT).show();
//
//            posProductSelectionDialog.

        });

        Inventory = (Button)findViewById(R.id.btnInventory);
        Inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,InventoryForm.class))    ;
            }
        });

        Logout = (Button)findViewById(R.id.btnlogout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        Sales = (Button)findViewById(R.id.btnSales);
        Sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SalesForm.class));
            }
        });

        Transactions = (Button)findViewById(R.id.btnTransactions);
        Transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TransactionsForm.class));
            }
        });
        Items = (Button)findViewById(R.id.btnItems);
        Items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ItemsForm.class));
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
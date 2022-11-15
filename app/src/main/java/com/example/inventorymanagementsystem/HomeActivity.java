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
    private FirebaseUser firebaseUser;
    private Button Logout, Items,Sales,Transactions,Inventory, btnPOS;
    private Button btnTest;
    private View.OnClickListener menuButtonClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        tvBusinessName = findViewById(R.id.tvbusName);
//        tvBusinessName.setText(businessName);

        sharedPreferences = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        storeId = sharedPreferences.getString("storeId","");
        isSignIn = sharedPreferences.getBoolean("isSignIn",false);



        TextView date = findViewById(R.id.Date);
        date.setText(time() + "   "+ date());

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

        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "storeId_session: " + storeId + " userId_session: " + userId , Toast.LENGTH_SHORT).show();
            }
        });

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
                        startActivity(new Intent(HomeActivity.this,SalesForm.class));
                        break;

                    case R.id.btnPOS:
                        startActivity(new Intent(HomeActivity.this,POSActivity.class));
                        break;

                    default:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
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
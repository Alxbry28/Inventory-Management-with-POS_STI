package com.example.inventorymanagementsystem.views.staff;
import androidx.appcompat.app.AppCompatActivity;
import com.example.inventorymanagementsystem.R;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TransactionDetailsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);



    }
}
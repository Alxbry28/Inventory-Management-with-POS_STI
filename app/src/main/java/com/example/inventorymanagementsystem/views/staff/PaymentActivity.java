package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.MainActivity;
import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.interfaces.IEntityModelListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.SoldItem;
import com.example.inventorymanagementsystem.models.SoldItemReport;

public class PaymentActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId, productId;
    private ArrayList<CartItem> cartItemsArrayList;
    private CartLibrary cartLibrary;
    private double totalPrice = 0, totalChange = 0, totalAmount = 0;
    private int totalQty = 0;
    private TextView tvTotalPrice, tvTotalChange, tvTotalItems;
    private EditText etAmountReceived;
    private Button btnExactAmount, btn5Amount, btn10Amount,btn20Amount,
            btn50Amount,btn100Amount,btn200Amount, btn500Amount, btn1000Amount;
    private Button btnPayment, btnBack;
    private Sales sales, lastSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initComponents();

        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);

        sales = new Sales();
        lastSale = new Sales();

        sales.setStoreId(storeId);
        sales.setUserId(userId);

        cartLibrary = new CartLibrary();
        cartLibrary.setStoreId(storeId);
        cartLibrary.setUserId(userId);
        cartLibrary.setSqLiteDB(new SQLiteDB(this));
        cartItemsArrayList = cartLibrary.retrieveCartItems();
        cartLibrary.setCartItemArrayList(cartItemsArrayList);

        totalPrice = cartLibrary.totalCartPrice();
        tvTotalPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(totalPrice));
        showTotalText();
        initEvents();
    }

    private void showTotalText(){
        totalQty = cartLibrary.totalCartItems();
        tvTotalItems.setText("Items: " + cartLibrary.totalCartItems());
        totalChange= totalAmount - totalPrice;
        tvTotalChange.setText("Change: P"+ MoneyLibrary.toTwoDecimalPlaces(totalChange));
    }

    private void initComponents(){
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalChange = findViewById(R.id.tvTotalChange);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        etAmountReceived = findViewById(R.id.etAmountReceived);
        btnExactAmount = findViewById(R.id.btnExactAmount);
        btn5Amount = findViewById(R.id.btn5Amount);
        btn10Amount = findViewById(R.id.btn10Amount);
        btn20Amount = findViewById(R.id.btn20Amount);
        btn50Amount = findViewById(R.id.btn50Amount);
        btn100Amount = findViewById(R.id.btn100Amount);
        btn200Amount = findViewById(R.id.btn200Amount);
        btn500Amount = findViewById(R.id.btn500Amount);
        btn1000Amount  = findViewById(R.id.btn1000Amount);
        btnPayment  = findViewById(R.id.btnPayment);
        btnBack  = findViewById(R.id.btnBack);
    }

    private void initEvents(){
        btnExactAmount.setOnClickListener(v -> {
            totalAmount = cartLibrary.totalCartPrice();
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn5Amount.setOnClickListener(v->{
            totalAmount = 5;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn10Amount.setOnClickListener(v->{
            totalAmount = 10;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn20Amount.setOnClickListener(v->{
            totalAmount = 20;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn50Amount.setOnClickListener(v->{
            totalAmount = 50;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn100Amount.setOnClickListener(v->{
            totalAmount = 100;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn200Amount.setOnClickListener(v->{
            totalAmount = 200;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn500Amount.setOnClickListener(v->{
            totalAmount = 500;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btn1000Amount.setOnClickListener(v->{
            totalAmount = 1000;
            etAmountReceived.setText(MoneyLibrary.toTwoDecimalPlaces(totalAmount));
            showTotalText();
        });

        btnPayment.setOnClickListener(v->{
            if(totalChange < 0){
                Toast.makeText(this, "Unable to proceed to payment", Toast.LENGTH_SHORT).show();
                return;
            }

            sales.setReceiptNo(sales.GeneratedInvoiceNumber());
            sales.setAmountPayable(totalPrice);
            sales.setQuantity(totalQty);
            sales.setAmountReceived(totalAmount);
            sales.setAmountChange(totalChange);
            sales.Create(new TransactionStatusListener() {
                @Override
                public void checkStatus(boolean status) {
                    ArrayList<SoldItem> soldItems = cartLibrary.getConvertedSoldItemArray();
                    for (int i = 0; i < soldItems.size(); i++) {

                        SoldItem soldItem = soldItems.get(i);
                        soldItem.setReceiptNo(sales.getReceiptNo());
                        soldItem.setSalesId(sales.getId());
                        soldItem.Create(new TransactionStatusListener() {
                            @Override
                            public void checkStatus(boolean status) {
                                Toast.makeText(PaymentActivity.this, "Sold Item Created", Toast.LENGTH_SHORT).show();
                            }
                        });

                        SoldItemReport soldItemReport = new SoldItemReport();
                        soldItemReport.setSoldItemToReport(soldItem);
                        soldItemReport.Update(new TransactionStatusListener() {
                            @Override
                            public void checkStatus(boolean status) {

                            }
                        });

                        Product productSold = new Product();
                        productSold.setId(soldItem.getProductId());
                        productSold.setQuantity(soldItem.getQuantity());
                        productSold.GetById(new ProductModelListener() {
                            @Override
                            public void retrieveProduct(Product product) {
                                int qtyResult = product.getQuantity() - productSold.getQuantity();
                                product.setQuantity(qtyResult);
                                product.Update(new TransactionStatusListener() {
                                    @Override
                                    public void checkStatus(boolean status) {
                                        Toast.makeText(PaymentActivity.this, "product Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void getProductList(ArrayList<Product> productArrayList) {

                            }
                            
                        });

                    }
                }
            });

           if(cartLibrary.clear()){
                startActivity(new Intent(PaymentActivity.this, POSItemActivity.class));
                finish();
           }

        });

        btnBack.setOnClickListener(v->{
            startActivity(new Intent(PaymentActivity.this, CartActivity.class));
             finish();
        });

        etAmountReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    totalAmount = Double.parseDouble(etAmountReceived.getText().toString());
                    showTotalText();
                }
            }
        });

    }

}
package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.inventorymanagementsystem.adapters.POSRCVAdapter;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class POSItemActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private RecyclerView rcPOSProductItem;
    private TextView tvPOSItemMsg;
    private Product product;
    private ArrayList<Product> productList;
    private ArrayList<Product> productSelectedList;
    private Button btnCheckout,btnCart;
    private CartLibrary cartLibrary;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positem);
        productSelectedList = new ArrayList<>();
        product = new Product();
        cartLibrary = new CartLibrary();

        sharedPreferences = getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE);

        businessName = sharedPreferences.getString("businessName",null);
        storeId = sharedPreferences.getString("storeId",null);
        userId = sharedPreferences.getString("userId",null);
        product.setUserId(userId);
        product.setStoreId(storeId);

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCart = findViewById(R.id.btnCart);
        rcPOSProductItem = findViewById(R.id.rcPOSProductItem);
        POSRCVAdapter posRCVAdapter = new POSRCVAdapter();
        product.GetAll(new ProductModelListener() {
            @Override
            public void retrieveProduct(Product product) {


            }

            @Override
            public void getProductList(ArrayList<Product> productArrayList) {
                if(!productArrayList.isEmpty()){
                    productList = productArrayList;
                    if(!(productList == null || productList.isEmpty())){

                        posRCVAdapter.setContext(POSItemActivity.this);
                        posRCVAdapter.setProductList(productArrayList);
                        rcPOSProductItem.setAdapter(posRCVAdapter);

                        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(POSItemActivity.this);
                        rcPOSProductItem.setLayoutManager(rcvLayoutManager);
                        rcPOSProductItem.setItemAnimator(new DefaultItemAnimator());
                    }
                }
            }
        });

        posRCVAdapter.setPosSelectedItemListener(new POSRCVAdapter.POSSelectedItemListener() {
            @Override
            public void getSelectedItem(Product product) {
                productSelectedList.add(product);
                totalPrice = productSelectedList.stream().filter(product1 -> product1.getPrice() > 0).mapToDouble(Product::getPrice).sum();

//                ArrayList<Product> cartProducts = cartLibrary.getProductArrayList();
                Map<String, List<Product>> groupProductByID = productSelectedList.stream().collect(Collectors.groupingBy(Product::getId));

                groupProductByID.forEach((id, products) ->{
//                 int index = Product.findIndexById(cartProducts, id);
//                    products.forEach(product1 -> {
////                        Toast.makeText(POSItemActivity.this, id + " " + product1.getName(), Toast.LENGTH_SHORT).show();
//                    });
                });

//                cartLibrary.setProductArrayList(cartProducts);
                btnCheckout.setText(productSelectedList.size() + " Items = P" + totalPrice);
            }
        });

        btnCart.setOnClickListener(v->{
            startActivity(new Intent(POSItemActivity.this, CartActivity.class));
//            finish();
        });

    }
}
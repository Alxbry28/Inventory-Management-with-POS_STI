package com.example.inventorymanagementsystem.views.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.inventorymanagementsystem.adapters.POSRCVAdapter;
import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.interfaces.POSSelectedItemListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.MainActivity;
public class POSItemActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId;
    private RecyclerView rcPOSProductItem;
    private TextView tvPOSItemMsg;
    private Product product;
    private ArrayList<Product> productList;
    private ArrayList<Product> productSelectedList;
    private Button btnCheckout,btnCart, btnBack;
    private CartLibrary cartLibrary;
    private double totalPrice = 0;
    private int totalQty = 0;
    private ArrayList<Product> cartProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positem);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((View v) -> {
                    startActivity(new Intent(POSItemActivity.this, HomeActivity.class));
                    finish();
                }
        );

        product = new Product();
        cartLibrary = new CartLibrary();
        cartLibrary.setSqLiteDB(new SQLiteDB(POSItemActivity.this));
        ArrayList<CartItem> cartItemsArrayList = cartLibrary.retrieveCartItems();
        cartLibrary.setCartItemArrayList(cartItemsArrayList);
        cartProducts = new ArrayList<>();

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

        if(cartItemsArrayList.size() > 0){
            cartProducts = cartLibrary.getConvertedProductItemArray();
            showTotal();
        }

        posRCVAdapter.setPosSelectedItemListener(new POSSelectedItemListener() {
            @Override
            public void getSelectedItem(Product product) {
                int tempQty = product.getQuantity();

                if(tempQty <= 0){
                    Toast.makeText(POSItemActivity.this, "The quantity of this is 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(cartProducts.size() < 0){
                    Product productToCart = new Product();
                    productToCart.setId(product.getId());
                    productToCart.setStoreId(product.getStoreId());
                    productToCart.setPrice(product.getPrice());
                    productToCart.setCategory(product.getCategory());
                    productToCart.setName(product.getName());
                    productToCart.setQuantity(1);
                    cartProducts.add(productToCart);

                    totalPrice = cartProducts.stream().filter(product1 -> product1.GetComputedTotalPrice() > 0).mapToDouble(Product::GetComputedTotalPrice).sum();

                    double roundOffPrice = (double) Math.round(totalPrice * 100) / 100;

                    totalQty = cartProducts.stream().filter(product1 -> product1.getQuantity() > 0).mapToInt(Product::getQuantity).sum();

                    btnCheckout.setText(totalQty + " Items = P" + roundOffPrice);
                }
                else{
                    int index = Product.findIndexById(cartProducts, product.getId());
                    if(index < 0){
                        Product productToCart = new Product();
                        productToCart.setId(product.getId());
                        productToCart.setStoreId(product.getStoreId());
                        productToCart.setPrice(product.getPrice());
                        productToCart.setCategory(product.getCategory());
                        productToCart.setName(product.getName());
                        productToCart.setQuantity(1);
                        cartProducts.add(productToCart);
                    }
                    else{
                        Product editProduct =  cartProducts.get(index);
                        if(tempQty == editProduct.getQuantity()){
                            Toast.makeText(POSItemActivity.this, "The quantity of this is equal to inventory quantity", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        else{
                            editProduct.setQuantity(editProduct.getQuantity() + 1);
                            cartProducts.set(index,editProduct);
                        }
                    }

                    showTotal();
                }

            }
        });


        btnCheckout.setOnClickListener( v->{
            cartLibrary.clear();
            cartLibrary.setProductArrayList(cartProducts);
            ArrayList<CartItem> cartItems = cartLibrary.getConvertedCartItemArray();
            cartLibrary.setCartItemArrayList(cartItems);
            cartLibrary.saveCartItems();

            startActivity(new Intent(POSItemActivity.this, CartActivity.class));
//            finish();
        });

        btnCart.setOnClickListener(v->{
            cartLibrary.clear();
            cartProducts.clear();
            showTotal();
            Toast.makeText(this, "Cart clear", Toast.LENGTH_SHORT).show();
        });

    }

    private void showTotal(){
        totalPrice = cartProducts.stream().filter(product1 -> product1.GetComputedTotalPrice() > 0).mapToDouble(Product::GetComputedTotalPrice).sum();

        double roundOffPrice = (double) Math.round(totalPrice * 100) / 100;

        totalQty = cartProducts.stream().filter(product1 -> product1.getQuantity() > 0).mapToInt(Product::getQuantity).sum();

        btnCheckout.setText(totalQty + " Items = P" + roundOffPrice);
    }

}
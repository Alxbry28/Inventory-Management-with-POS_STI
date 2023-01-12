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
import com.example.inventorymanagementsystem.dialogs.NotifyStockDialog;
import com.example.inventorymanagementsystem.enums.StockNotification;
import com.example.inventorymanagementsystem.interfaces.POSSelectedItemListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.CartLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Locale;
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
    private NotifyStockDialog notifyStockDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positem);
//        getSupportActionBar().hide();
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
                        notifyStockDialog = new NotifyStockDialog(POSItemActivity.this);

                        posRCVAdapter.setContext(POSItemActivity.this);

                        List<Product> tempsortedProduct = productArrayList.stream()
                        .sorted((p1,p2)-> p1.getName().toUpperCase().compareTo(p2.getName().toUpperCase()))
                                .collect(Collectors.toList());
                        ArrayList<Product> sortedProduct = new ArrayList<>(tempsortedProduct);

                        List<Product> tempRestock = tempsortedProduct.stream()
                                .filter(product1 -> product1.getQuantity() <= product1.getRestock())
                                .collect(Collectors.toList());

                        List<Product> tempOutOfStock = tempsortedProduct.stream()
                                .filter(product1 -> product1.getQuantity() <= 5)
                                .collect(Collectors.toList());

                        List<Product> tempGood = tempsortedProduct.stream()
                                .filter(product1 -> product1.getQuantity() > product1.getRestock())
                                .collect(Collectors.toList());

                        if(tempOutOfStock.size() > 0){
                            notifyStockDialog.setStockNotification(StockNotification.NOSTOCK);
                            notifyStockDialog.setMessage("Warning. Need to reorder items. There are "+tempOutOfStock.size()+" items either out of stock or near to out of stock");
                        }
                        else if(tempRestock.size() > 0){
                            notifyStockDialog.setStockNotification(StockNotification.RESTOCK);
                            notifyStockDialog.setMessage("There are "+tempRestock.size()+" items that are need to re order items");
                        }
                        else if(tempOutOfStock.size() == 0 && tempRestock.size() == 0  && tempGood.size() > 0){
                            notifyStockDialog.setStockNotification(StockNotification.GOOD);
                            notifyStockDialog.setMessage("There are "+tempGood.size()+". All stocks are in good condition");
                        }

                        notifyStockDialog.show(getSupportFragmentManager(),"SHOW_NOTIF_DIALOG");



                        posRCVAdapter.setProductList(sortedProduct);
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
                    Toast.makeText(POSItemActivity.this, "Out of Stock", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(POSItemActivity.this, "Out of stock.", Toast.LENGTH_SHORT).show();
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
            if (totalQty == 0)
            {
                Toast.makeText(POSItemActivity.this, "Cart is empty.", Toast.LENGTH_SHORT).show();
            }
            else {
                cartLibrary.clear();
                cartLibrary.setProductArrayList(cartProducts);
                ArrayList<CartItem> cartItems = cartLibrary.getConvertedCartItemArray();
                cartLibrary.setCartItemArrayList(cartItems);
                cartLibrary.saveCartItems();
                startActivity(new Intent(POSItemActivity.this, CartActivity.class));
            }
            //finish();
            });

        btnCart.setOnClickListener(v->{
            cartLibrary.clear();
            cartProducts.clear();
            showTotal();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });

    }

    private void initRCVPOSItem(){

    }

    private void showTotal(){
        totalPrice = cartProducts.stream().filter(product1 -> product1.GetComputedTotalPrice() > 0).mapToDouble(Product::GetComputedTotalPrice).sum();

        double roundOffPrice = (double) Math.round(totalPrice * 100) / 100;

        totalQty = cartProducts.stream().filter(product1 -> product1.getQuantity() > 0).mapToInt(Product::getQuantity).sum();

        btnCheckout.setText(totalQty + " Items = P" + roundOffPrice);
    }

}
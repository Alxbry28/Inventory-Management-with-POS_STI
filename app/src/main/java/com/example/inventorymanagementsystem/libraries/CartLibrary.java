package com.example.inventorymanagementsystem.libraries;

import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public class CartLibrary {
    private SQLiteDB sqLiteDB;
    private ArrayList<Product> productArrayList;
    private ArrayList<CartItem> cartItemArrayList;
    private String storeId;

    public ArrayList<CartItem> getConvertedCartItemArray(){
        ArrayList<CartItem> cartItemArrayList = new ArrayList<>();
        for (int i = 0; i < productArrayList.size(); i++) {
            Product product = productArrayList.get(i);
            CartItem cartItem = new CartItem();
            cartItem.setProductId(product.getId());
            cartItem.setName(product.getName());
            cartItem.setCategory(product.getCategory());
            cartItem.setQuantity(product.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setTotalPrice(product.GetComputedTotalPrice());
            cartItemArrayList.add(cartItem);
        }
        return cartItemArrayList;
    }

    public boolean saveCartItems(){
        int createdCount = 0;
        for (int i = 0; i < cartItemArrayList.size(); i++) {
            CartItem cartItem = cartItemArrayList.get(i);
            cartItem.setSqLiteDB(sqLiteDB);
            if (cartItem.Create()) createdCount++;
        }
        return (createdCount > 0) ? true : false;
    }

    public boolean clear(){
        CartItem cartItem = new CartItem();
        cartItem.setSqLiteDB(sqLiteDB);
        return cartItem.ClearAll();
    }

    public ArrayList<CartItem> retrieveCartItems(){
        CartItem cartItem = new CartItem();
        cartItem.setSqLiteDB(sqLiteDB);
        return cartItem.GetAll();
    }

    public ArrayList<Product> getConvertedProductItemArray(){
        ArrayList<Product> productArrayList = new ArrayList<>();
        for (int i = 0; i < cartItemArrayList.size(); i++) {
            CartItem cartItem = cartItemArrayList.get(i);
            Product product = new Product();
            product.setStoreId(storeId);
            product.setId(cartItem.getProductId());
            product.setName(cartItem.getName());
            product.setCategory(cartItem.getCategory());
            product.setQuantity(cartItem.getQuantity());
            product.setPrice(cartItem.getPrice());
            productArrayList.add(product);
        }
        return productArrayList;
    }

    public SQLiteDB getSqLiteDB() {
        return sqLiteDB;
    }

    public void setSqLiteDB(SQLiteDB sqLiteDB) {
        this.sqLiteDB = sqLiteDB;
    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    public ArrayList<CartItem> getCartItemArrayList() {
        return cartItemArrayList;
    }

    public void setCartItemArrayList(ArrayList<CartItem> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}

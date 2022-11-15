package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public class ProductRCVAdapter extends RecyclerView.Adapter<ProductRCVAdapter.ProductsViewHolder>  {

    private ArrayList<Product> productList;
    private Context context;
    private Activity activity;

    public class ProductsViewHolder extends RecyclerView.ViewHolder{

        TextView tvProductName, tvCategory, tvPrice, tvQuantity;
        Button btnEdit, btnDelete;
        View view;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

    }

    @NonNull
    @Override
    public ProductRCVAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_product_item, parent, false);
        return new ProductRCVAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvCategory.setText("Category: " + product.getCategory());
        holder.tvQuantity.setText("Qty: " + String.valueOf(product.getQuantity()));
        holder.tvPrice.setText("P" + String.valueOf(product.getPrice()));
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}

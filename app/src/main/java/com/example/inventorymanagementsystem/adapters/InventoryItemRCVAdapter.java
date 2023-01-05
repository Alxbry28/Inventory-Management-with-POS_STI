package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.SoldItem;

import java.util.ArrayList;

public class InventoryItemRCVAdapter extends RecyclerView.Adapter<InventoryItemRCVAdapter.InventoryItemViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList<Product> tempProductList;

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_inventory_selection_item, parent, false);
        return new InventoryItemRCVAdapter.InventoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemViewHolder holder, int position) {
        Product product = tempProductList.get(position);
        holder.tvProductCategory.setText(product.getCategory());
        holder.tvCategoryStocks.setText("Stocks: " +product.getQuantity());
        holder.cvInventoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tempProductList.size();
    }

    public class InventoryItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductCategory, tvCategoryStocks;
        CardView cvInventoryItem;
        View view;

        public InventoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvCategoryStocks = itemView.findViewById(R.id.tvCategoryStocks);
            cvInventoryItem = itemView.findViewById(R.id.cvInventoryItem);
            view = itemView;
        }
    }

}

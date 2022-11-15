package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Transaction;

import java.util.List;

public class SalesRCVAdapter extends RecyclerView.Adapter<SalesRCVAdapter.SalesViewHolder> {

    private List<Sales> salesList;
    private Context context;
    private Activity activity;

    public class SalesViewHolder extends RecyclerView.ViewHolder{

        //initialize components
        TextView tvSalesName, tvSalesPrice, tvSalesQuantity;
        View view;

        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            tvSalesName = itemView.findViewById(R.id.tvSalesName);
            tvSalesPrice = itemView.findViewById(R.id.tvSalesPrice);
            tvSalesQuantity = itemView.findViewById(R.id.tvSalesQuantity);
        }
    }

    @NonNull
    @Override
    public SalesRCVAdapter.SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_sales_item, parent, false);
        return new SalesRCVAdapter.SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesRCVAdapter.SalesViewHolder holder, int position) {
        Sales sales = salesList.get(position);
        holder.tvSalesName.setText(sales.getCreatedAt());
        holder.tvSalesPrice.setText("P" + String.valueOf(sales.getTotal_price()));
        holder.tvSalesQuantity.setText("Qty: " + String.valueOf(sales.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public List<Sales> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<Sales> salesList) {
        this.salesList = salesList;
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

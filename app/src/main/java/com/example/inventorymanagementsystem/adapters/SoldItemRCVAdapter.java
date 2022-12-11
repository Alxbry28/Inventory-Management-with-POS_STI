package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.SoldItem;

import java.util.ArrayList;

public class SoldItemRCVAdapter extends RecyclerView.Adapter<SoldItemRCVAdapter.SoldItemViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList<SoldItem> soldItemArrayList;

    @NonNull
    @Override
    public SoldItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_transaction_details_solditems, parent, false);
        return new SoldItemRCVAdapter.SoldItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldItemViewHolder holder, int position) {
        SoldItem soldItem = soldItemArrayList.get(position);
        holder.tvItemName.setText(soldItem.getName());
        holder.tvQuantity.setText("Quantity: " +soldItem.getQuantity());
        holder.tvItemPrice.setText("Price: P" + MoneyLibrary.toTwoDecimalPlaces(soldItem.getProductPrice()));
        holder.tvItemSubtotal.setText("Subtotal: P" + MoneyLibrary.toTwoDecimalPlaces(soldItem.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return soldItemArrayList.size();
    }

    public class SoldItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemName, tvItemPrice, tvQuantity, tvItemSubtotal;
        View view;

        public SoldItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvItemSubtotal = itemView.findViewById(R.id.tvItemSubtotal);
            view = itemView;
        }
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

    public ArrayList<SoldItem> getSoldItemArrayList() {
        return soldItemArrayList;
    }

    public void setSoldItemArrayList(ArrayList<SoldItem> soldItemArrayList) {
        this.soldItemArrayList = soldItemArrayList;
    }
}

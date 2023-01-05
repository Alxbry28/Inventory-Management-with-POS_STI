package com.example.inventorymanagementsystem.views.staff;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventorymanagementsystem.R;

public class ItemListVH extends RecyclerView.ViewHolder
{
    public TextView  itemname,quantity,price,measurement, option;

    public ItemListVH(@NonNull View itemView) {
        super(itemView);
        itemname = itemView.findViewById(R.id.itemname);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
        measurement = itemView.findViewById(R.id.measurement);
        option = itemView.findViewById(R.id.option);
    }
}

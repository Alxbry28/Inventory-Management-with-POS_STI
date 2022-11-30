package com.example.inventorymanagementsystem.views.staff;

import  android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import com.example.inventorymanagementsystem.R;
public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    ArrayList<ItemList> list = new ArrayList<>();
    public  ItemListAdapter (Context ctx)

    {
        this.context = ctx;
    }
    public void setItems(ArrayList<ItemList>il)
    {
        list.addAll(il);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ItemListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position)
    {
        ItemListVH vh = (ItemListVH) holder;
        ItemList il = list.get(position);
        vh.itemname.setText("Item: " + il.getItemname());
        vh.quantity.setText("Quantity: " +il.getQuantity());
        vh.price.setText("Price: ₱" +il.getPrice());
        vh.measurement.setText("Measurement: " +il.getMeasurement());
        vh.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, vh.option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(item->
                {
                    switch (item.getItemId())
                    {
                        case R.id.menuedit:
                            Intent intent= new Intent(context, AddItemForm.class);
                            intent.putExtra("Edit",list);
                            context.startActivity(intent);
                            break;

                        case R.id.menuremove:
                            DBItemList DB = new DBItemList();
                            DB.remove(il.getKey()).addOnSuccessListener(suc->
                            {
                                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                            }).addOnFailureListener(er->
                            {
                                Toast.makeText(context,""+er.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                            break;
                    }
                    return false;

                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

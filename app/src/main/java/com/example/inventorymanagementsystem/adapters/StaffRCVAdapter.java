package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Staff;

import java.util.ArrayList;

public class StaffRCVAdapter extends RecyclerView.Adapter<StaffRCVAdapter.StaffViewHolder> {

    private ArrayList<Staff> staffArrayList;
    private Context context;
    private Activity activity;

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_staff_item, parent, false);
        return new StaffRCVAdapter.StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffArrayList.get(position);
        holder.tvFullname.setText(staff.getFullname());
        holder.tvPosition.setText(staff.getPosition());
        holder.btnDeleteStaff.setOnClickListener(v -> {
            Toast.makeText(context, "Delete Staff", Toast.LENGTH_SHORT).show();
        });

        holder.btnEditStaff.setOnClickListener(v -> {
            Toast.makeText(context, "Edit Staff", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder{

        TextView tvFullname, tvPosition;
        ImageButton btnEditStaff, btnDeleteStaff;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFullname = itemView.findViewById(R.id.tvFullname);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            btnEditStaff = itemView.findViewById(R.id.btnEditStaff);
            btnDeleteStaff = itemView.findViewById(R.id.btnDeleteStaff);

        }
    }

    public ArrayList<Staff> getStaffArrayList() {
        return staffArrayList;
    }

    public void setStaffArrayList(ArrayList<Staff> staffArrayList) {
        this.staffArrayList = staffArrayList;
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

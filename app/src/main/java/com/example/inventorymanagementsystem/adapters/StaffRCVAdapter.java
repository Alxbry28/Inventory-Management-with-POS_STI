package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.views.staff.*;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.User;

import java.util.ArrayList;

public class StaffRCVAdapter extends RecyclerView.Adapter<StaffRCVAdapter.StaffViewHolder> {

    private ArrayList<Staff> staffArrayList;
    private Context context;
    private Activity activity;
    private String currentUserId;

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
        if(currentUserId.equals(staff.getUserId())){
            holder.btnDeleteStaff.setVisibility(View.GONE);
        }

        if(staff.getPosition().equals("Business Owner")){
            holder.btnDeleteStaff.setVisibility(View.GONE);
        }

        holder.btnDeleteStaff.setOnClickListener(v -> {
            AlertDialog.Builder alertDeleteDialog = new AlertDialog.Builder(context);
            alertDeleteDialog.setTitle("Delete Staff");
            alertDeleteDialog.setMessage("Are you sure do you want to delete this staff?");
            alertDeleteDialog.setNegativeButton("Cancel",null);
            alertDeleteDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    staff.Delete(new TransactionStatusListener() {
                        @Override
                        public void checkStatus(boolean status) {
                            User user = new User();
                            user.setId(staff.getUserId());
                            user.Delete(new TransactionStatusListener() {
                                @Override
                                public void checkStatus(boolean status) {
                                    Toast.makeText(context, "Successfully Deleted Staff", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            alertDeleteDialog.show();

        });

        holder.btnEditStaff.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditStaffActivity.class);
            intent.putExtra("isEditStaff", true);
            intent.putExtra("staffId", staff.getId());
            intent.putExtra("userId", staff.getUserId());
            context.startActivity(intent);
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

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
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

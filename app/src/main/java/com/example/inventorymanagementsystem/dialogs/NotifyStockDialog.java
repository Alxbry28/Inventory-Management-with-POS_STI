package com.example.inventorymanagementsystem.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.inventorymanagementsystem.R;

public class NotifyStockDialog extends AppCompatDialogFragment {

    private Context context;
    private String message;
    private int resourceImage;

    public NotifyStockDialog(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_notify_product_stocks,null);
        ImageView ivNotifImage = view.findViewById(R.id.ivNotifImage);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        ivNotifImage.setBackgroundResource();

        return builder.create();
    }
}

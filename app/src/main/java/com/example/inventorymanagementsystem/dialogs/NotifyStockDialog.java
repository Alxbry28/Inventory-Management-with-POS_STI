package com.example.inventorymanagementsystem.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.inventorymanagementsystem.enums.StockNotification;

public class NotifyStockDialog extends AppCompatDialogFragment {

    private Context context;
    private String message;
    private StockNotification stockNotification;

    public NotifyStockDialog(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Stock Notification");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_notify_product_stocks,null);

        ImageView ivNotifImage = view.findViewById(R.id.ivNotifImage);
        TextView tvMessage = view.findViewById(R.id.tvMessage);

        if(message.isEmpty() || message == null){
            tvMessage.setText(stockNotification.Message);
        }
        else{
            tvMessage.setText(message);
        }


        ivNotifImage.setBackgroundResource(stockNotification.Icon);




        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StockNotification getStockNotification() {
        return stockNotification;
    }

    public void setStockNotification(StockNotification stockNotification) {
        this.stockNotification = stockNotification;
    }
}

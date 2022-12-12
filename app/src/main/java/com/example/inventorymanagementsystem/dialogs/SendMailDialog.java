package com.example.inventorymanagementsystem.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.services.MailerService;

public class SendMailDialog extends AppCompatDialogFragment {

    private Context context;
    private MailerService mailerService;

    public SendMailDialog(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_email_sales,null);
        EditText etEmail = view.findViewById(R.id.etEmail);
        builder.setView(view).setTitle("Send Mail Sales Report");
        builder.setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                dialog.dismiss();
        });

        builder.setPositiveButton("Send", (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                mailerService = new MailerService();
                mailerService.setContext(context);
                mailerService.setReceiverEmail(etEmail.getText().toString());
                mailerService.sendSalesReport();
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

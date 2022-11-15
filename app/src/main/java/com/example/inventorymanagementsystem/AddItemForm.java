package com.example.inventorymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

public class AddItemForm extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;
    private String businessName;
    private TextView tvBusinessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_form);
        sharedPreferences = getSharedPreferences(MainActivity.TAG,MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName",null);

        tvBusinessName = findViewById(R.id.tv1);
        tvBusinessName.setText(businessName);

        final EditText item = findViewById(R.id.itemname);
        final EditText quantity = findViewById(R.id.quantity);
        final EditText price = findViewById(R.id.price);
        final Spinner measurement = findViewById(R.id.spinner);
        Button add = findViewById(R.id.btnAdd);
        Button view = findViewById(R.id.btnViewInventory);
        Button viewinventory = findViewById(R.id.btnViewInventory);
        DBItemList DB = new DBItemList();
        ItemList il_edit = (ItemList)getIntent().getSerializableExtra("Edit");
        if (il_edit != null)
        {
            add.setText("Update");
            item.setText(il_edit.getItemname());
            quantity.setText(il_edit.getQuantity());
            price.setText(il_edit.getPrice());
            view.setVisibility(View.GONE);
        }
        else
        {
            add.setText("Add");
            view.setVisibility(View.VISIBLE);

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() ) {
                    Toast.makeText(AddItemForm.this, "Fill out all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    ItemList il = new ItemList(item.getText().toString(), quantity.getText().toString(), price.getText().toString(), measurement.getSelectedItem().toString());
                    if (il_edit == null) {
                        DB.add(il).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(AddItemForm.this, "Item Successfully Added", Toast.LENGTH_SHORT).show();
                            item.setText("");
                            quantity.setText("");
                            price.setText("");
                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(AddItemForm.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    else
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("itemname",item.getText().toString());
                            hashMap.put("quantity",quantity.getText().toString());
                            hashMap.put("price",price.getText().toString());
                            hashMap.put("measurement",measurement.getSelectedItem().toString());
                            DB.update(il_edit.getKey(), hashMap).addOnSuccessListener(suc->
                            {
                                Toast.makeText(AddItemForm.this,"Item Successfully Updated",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddItemForm.this, InventoryForm.class);
                                startActivity(intent);

                            }).addOnFailureListener(er->
                            {
                                Toast.makeText(AddItemForm.this,""+er.getMessage(),Toast.LENGTH_SHORT).show();
                            });

                    }
                }
            }
        });
        viewinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddItemForm.this,InventoryForm.class));
            }
        });
    }

}
package com.example.inventorymanagementsystem.views.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import com.example.inventorymanagementsystem.R;
public class InventoryForm extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemListAdapter adapter;
    DBItemList db;
    boolean isLoading = false;
    String key = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);
        Button home = findViewById(R.id.btnHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryForm.this, HomeActivity.class));
            }
        });

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ItemListAdapter(this);
        recyclerView.setAdapter(adapter);
        db = new DBItemList();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (totalItem < lastVisible)
                {
                    isLoading = true;
                    loadData();
                }
            }
        });

        Button add = findViewById(R.id.btnAddForm);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryForm.this, AddItemForm.class));
            }
        });
    }
    private void loadData()
    {
        db.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot)
            {
                ArrayList<ItemList> ils = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    ItemList il = data.getValue(ItemList.class);
                    il.setKey(data.getKey());
                    ils.add(il);
                    key = data.getKey();
                }
                adapter.setItems(ils);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error)
            {

            }
        });
    }
}
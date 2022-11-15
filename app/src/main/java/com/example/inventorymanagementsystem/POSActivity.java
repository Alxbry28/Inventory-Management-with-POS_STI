package com.example.inventorymanagementsystem;

import android.os.Bundle;

import com.example.inventorymanagementsystem.dialogs.POSProductSelectionDialog;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inventorymanagementsystem.databinding.ActivityPosactivityBinding;

public class POSActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPosactivityBinding binding;
    private POSProductSelectionDialog posProductSelectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
        binding = ActivityPosactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_posactivity);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posProductSelectionDialog.show(getSupportFragmentManager(),"dialog_choose_product");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_posactivity);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void initDialog(){
        posProductSelectionDialog = new POSProductSelectionDialog();
        posProductSelectionDialog.setContext(POSActivity.this);
    }
}
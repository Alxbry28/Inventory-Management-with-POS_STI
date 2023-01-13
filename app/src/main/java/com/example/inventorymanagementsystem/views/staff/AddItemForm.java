package com.example.inventorymanagementsystem.views.staff;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.enums.AppConstant;
import com.example.inventorymanagementsystem.interfaces.IStorageServiceListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.Validation;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.MainActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.inventorymanagementsystem.services.StorageService;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddItemForm extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String businessName, storeId, userId, productId;
    private TextView tvBusinessName, tvProductTransactionType;
    private EditText etItem, etProductName, etProductCategory, etPrice, etStocks,etReStocks;
    private Button btnAdd, btnBack, btnChoosePhoto, btnTakePhoto;
    private Product product;
    private boolean isEditProduct;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectedUri;
    private ImageView ivProductImage;
    private StorageService storageService;
    private String selectedUrl;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_form);

        storageService = new StorageService(AddItemForm.this);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), (Uri result) -> {
                if (result != null) {
                    Intent intent = new Intent(AddItemForm.this, CropperActivity.class);
                    intent.putExtra("DATA", result.toString());
                    startActivityForResult(intent, 101);
                }
        });

        boolean isEditProduct = getIntent().getBooleanExtra("isEditProduct", false);

        product = new Product();
        sharedPreferences = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);
        businessName = sharedPreferences.getString("businessName", null);
        storeId = sharedPreferences.getString("storeId", null);
        userId = sharedPreferences.getString("userId", null);

        initComponents();
        isEditProduct = getIntent().hasExtra("isEditProduct");
        if (isEditProduct) {
            product.setId(getIntent().getStringExtra("productId"));
            product.setStoreId(storeId);
            product.GetById(new ProductModelListener() {
                @Override
                public void retrieveProduct(Product p) {
                    String imageUrl = (product.getImageUrl() == null) ? AppConstant.IMAGE.Value : p.getImageUrl();
                    Picasso.get().load(imageUrl).into(ivProductImage);
                    etProductName.setText(p.getName());
                    product.setCreated_at(p.getCreated_at());
                    product.setCreated_time(p.getCreated_time());

                    etReStocks.setText(String.valueOf(p.getRestock()));
                    etProductCategory.setText(p.getCategory());
                    etPrice.setText(String.valueOf(p.getPrice()));
                    etStocks.setText(String.valueOf(p.getQuantity()));
                }

                @Override
                public void getProductList(ArrayList<Product> productArrayList) {

                }
            });

            String transType = "Edit Product";
            tvProductTransactionType.setText(transType);
            btnAdd.setText("Save");

        }

        initEventButtons();
        tvBusinessName.setText(businessName);

    }

    private void initComponents() {
        tvProductTransactionType = findViewById(R.id.tvProductTransactionType);
        btnBack = findViewById(R.id.btnBackToProducts);
        btnChoosePhoto = findViewById(R.id.btnChoosePhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);

        btnAdd = findViewById(R.id.btnAddItemProduct);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvBusinessName = findViewById(R.id.tv1);
        etProductName = findViewById(R.id.etProductName);
        etProductCategory = findViewById(R.id.etProductCategory);
        etStocks = findViewById(R.id.etStocks);
        etReStocks = findViewById(R.id.etReStocks);
        etPrice = findViewById(R.id.etPrice);
    }
    // Image Selection Activity
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1234:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == -1 && requestCode == 101) {
                String result = data.getStringExtra("RESULT");
                Uri resultUri = null;
                if (result != null) {
                    resultUri = Uri.parse(result);
                    selectedUri = resultUri;
                    Picasso.get().load(selectedUri).into(ivProductImage);
                }
            } else if (requestCode == 100) {
                Picasso.get().load(selectedUri).into(ivProductImage);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

    }

    private void initEventButtons() {
        btnAdd.setOnClickListener(v -> {

            if (TextUtils.isEmpty(etStocks.getText().toString()) || TextUtils.isEmpty(etReStocks.getText().toString()) || TextUtils.isEmpty(etPrice.getText().toString())
                    || TextUtils.isEmpty(etProductName.getText().toString()) || TextUtils.isEmpty((etProductCategory.getText().toString()))) {
                Toast.makeText(this, "Empty fields. Cannot proceed.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Validation.isValueDouble(etPrice.getText().toString())) {
                Toast.makeText(AddItemForm.this, "Price is not valid", Toast.LENGTH_SHORT).show();
                return;
            }

            isEditProduct = getIntent().hasExtra("isEditProduct");
            product.setStoreId(storeId);
            product.setUserId(userId);
            product.setQuantity(Integer.parseInt(etStocks.getText().toString()));
            product.setRestock(Integer.parseInt(etReStocks.getText().toString()));
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));

            product.setName(etProductName.getText().toString());
            product.setCategory(etProductCategory.getText().toString());

            if (product.getQuantity() <= 0) {
                Toast.makeText(this, "Invalid input for quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (product.getPrice() <= 0) {
                Toast.makeText(this, "Invalid input for price", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedUri == null) {
                product.setImageUrl(AppConstant.IMAGE.Value);
                if (isEditProduct) {
                    product.Update(status -> {
                        if (status) {
                            Toast.makeText(AddItemForm.this, "Edit Product Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItemForm.this, ItemsForm.class));
                            finish();
                        }
                    });
                } else {
                    product.Create(status -> {
                        if (status) {
                            Toast.makeText(AddItemForm.this, "Product Successfully Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItemForm.this, ItemsForm.class));
                            finish();
                        }
                    });
                }
            } else {
                storageService.upload("products/" + storeId, selectedUri, new IStorageServiceListener() {
                    @Override
                    public void getUriImage(Uri imageUri) {
                        product.setImageUrl(imageUri.toString());
                        if (isEditProduct) {
                            product.Update(status -> {
                                if (status) {
                                    Toast.makeText(AddItemForm.this, "Edit Product Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddItemForm.this, ItemsForm.class));
                                    finish();
                                }
                            });
                        } else {
                            product.Create(status -> {
                                if (status) {
                                    Toast.makeText(AddItemForm.this, "Product Successfully Added", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddItemForm.this, ItemsForm.class));
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void getUploadProgress(int progress) {
                        //Toast.makeText(AddItemForm.this, "progress: " + progress + "%", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnChoosePhoto.setOnClickListener((View view) -> {
            mGetContent.launch("image/*");
        });

        btnTakePhoto.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ) {
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, 1234);
                } else {
                    openCamera();
                }

            } else {
                openCamera();
            }

        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        selectedUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedUri);
        startActivityForResult(openCameraIntent, 100);
    }

}
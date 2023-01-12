package com.example.inventorymanagementsystem.interfaces;

import android.net.Uri;

public interface IStorageServiceListener {

    void getUriImage(Uri imageUri);
    void getUploadProgress(int progress);
}

package com.example.inventorymanagementsystem.services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageService {

    private FirebaseStorage firebaseStorage;
    private Context context;

    public StorageService(Context context){
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    public Uri upload(String storageDirectories, Uri uri){
        StorageReference storageReference = firebaseStorage.getReference(storageDirectories);
        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(uri));
        UploadTask uploadTask = reference.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return (!task.isSuccessful()) ? null : reference.getDownloadUrl();
            }
        });
        return urlTask.getResult();
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

}

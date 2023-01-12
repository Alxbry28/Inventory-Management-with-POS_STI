package com.example.inventorymanagementsystem.services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.interfaces.IStorageServiceListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class StorageService {

    private FirebaseStorage firebaseStorage;
    private Context context;

    public StorageService(Context context){
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
    }
//
//    public Uri upload(String storageDirectories, Uri uri){
//        StorageReference storageReference = firebaseStorage.getReference(storageDirectories);
//        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(uri));
//        UploadTask uploadTask = reference.putFile(uri);
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return (!task.isSuccessful()) ? null : reference.getDownloadUrl();
//            }
//        });
//        return urlTask.getResult();
//    }

    public void upload(String storageDirectories, Uri uri, final IStorageServiceListener iStorageServiceListener){
        String  tempExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        StorageReference storageReference = firebaseStorage.getReference(storageDirectories);
        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ tempExt);
        UploadTask uploadTask = reference.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return (!task.isSuccessful()) ? null : reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    iStorageServiceListener.getUriImage(downloadUri);
                }
            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                int percentageStatus = (int) progress;
                iStorageServiceListener.getUploadProgress(percentageStatus);
            }
        });

    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

}

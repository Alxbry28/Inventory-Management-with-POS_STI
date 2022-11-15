package com.example.inventorymanagementsystem.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsystem.interfaces.TransactionStatusListener;
import com.example.inventorymanagementsystem.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class AuthService {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private User user;

    private boolean status = false;
    private int intStatus = 0;

    public AuthService(){
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public AuthResult Login(){
        AuthResult authResult = null;
        Task<AuthResult> task = firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                boolean check = task.isSuccessful();
                Log.d("AuthService:41", "onComplete: " + task.isSuccessful());
                if (check){
                    //Redirect to activity after successful login
                    firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Log.d("AuthService:41", "Email Verified: " + firebaseUser.isEmailVerified());
                        intStatus = 1; // Email Verified
                    }else {
                        firebaseUser.sendEmailVerification();
                        intStatus = 2; // Not been verified, sending email verification
                    }
                }
                else {
                    intStatus = 0; // Failed
                }
            }
        });

    try {
        authResult = Tasks.await(task,500, TimeUnit.MILLISECONDS);
        authResult.wait();
//        authResult.
    }
    catch (Exception e){
        e.printStackTrace();
    }
        return  authResult;
    }

    public void Register(final TransactionStatusListener transactionStatusListener){
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(task -> {
                        transactionStatusListener.checkStatus(task.isSuccessful());
                    }
         );
    }

    public boolean ResetPassword(){
        status = false;
        firebaseAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                status = task.isSuccessful();
            }
        });
        return status;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

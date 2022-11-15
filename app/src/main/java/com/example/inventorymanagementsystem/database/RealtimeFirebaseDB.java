package com.example.inventorymanagementsystem.database;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.Staff;
import com.example.inventorymanagementsystem.models.Store;
import com.example.inventorymanagementsystem.models.User;
import com.example.inventorymanagementsystem.models.SuperAdmin;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeFirebaseDB {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public RealtimeFirebaseDB(){
       this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public DatabaseReference UserTable(){
        return firebaseDatabase.getInstance().getReference(User.TABLE);
    }

    public DatabaseReference StaffTable(){
        return firebaseDatabase.getInstance().getReference(Staff.TABLE);
    }

    public DatabaseReference SuperAdminTable(){
        return firebaseDatabase.getInstance().getReference(SuperAdmin.TABLE);
    }

    public DatabaseReference StoreTable(){
        return firebaseDatabase.getInstance().getReference(Store.TABLE);
    }

    public DatabaseReference ProductsTable(){
        return firebaseDatabase.getInstance().getReference(Product.TABLE);
    }


}

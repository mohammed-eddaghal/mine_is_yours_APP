package com.ensias.mine_is_yoursapp;


import android.util.Log;

import androidx.annotation.NonNull;

import com.ensias.mine_is_yoursapp.Tool;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseTraitement {

    public static User getUserByID(@NonNull String keyUser,String path, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference) throws InterruptedException {
        final User user3=new User();

        databaseReference = firebaseDatabase.getReference(path);
        databaseReference.child(keyUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            //User user3=new User();
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase static", "Error getting data", task.getException());
                }
                else {
                    user3.cloneUser(task.getResult().getValue(User.class));
                    user3.setLastName(task.getResult().getValue(User.class).getLastName());
                    Log.d("firebase test 0", user3.toString());
                    Log.d("firebase test 1", task.getResult().getValue(User.class).toString());

                }
            }
        });
        Thread.sleep(500);
        Log.d("firebase test 2", user3.toString());
        return user3;
    }

    public static void getListTool(@NonNull String toolName,String path, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference) throws InterruptedException {

        databaseReference = firebaseDatabase.getReference(path);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase static", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase test 12", ""+task.getResult().getValue());

                }
            }
        });
    }



}

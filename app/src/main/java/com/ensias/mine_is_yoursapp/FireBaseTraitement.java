package com.ensias.mine_is_yoursapp;


import android.util.Log;

import androidx.annotation.NonNull;

import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireBaseTraitement {

    public static User getUserByID(@NonNull String keyUser, String path, FirebaseDatabase firebaseDatabase) throws InterruptedException {
        final User user3=new User();
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);
        databaseReference.child(keyUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            //User user3=new User();
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase static", "Error getting data", task.getException());
                }
                else {
                    user3.cloneUser(task.getResult().getValue(User.class));
                    //user3.setLastName(task.getResult().getValue(User.class).getLastName());
                    Log.d("firebase test 0", user3.toString());
                    Log.d("firebase test 1", task.getResult().getValue(User.class).toString());

                }
            }
        });
        Thread.sleep(500);
        Log.d("firebase test 2", user3.toString());
        return user3;
    }





}

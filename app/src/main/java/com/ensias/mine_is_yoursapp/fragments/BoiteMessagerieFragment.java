package com.ensias.mine_is_yoursapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.UserAdapter;
import com.ensias.mine_is_yoursapp.model.Message;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class BoiteMessagerieFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<String> idList;

    private FirebaseUser userFirebase;
    private DatabaseReference databaseReference;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_boite_messagerie, container, false);

        recyclerView = view.findViewById(R.id.list_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       userFirebase = FirebaseAuth.getInstance().getCurrentUser();

        readMessages();


        return view;
    }

    private void readMessages() {
        idList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    Message message = snapshot.getValue(Message.class);
                    if ( message.getIdFrom().equals(userFirebase.getUid())){
                        idList.add(message.getIdTo());
                    }
                    if ( message.getIdTo().equals(userFirebase.getUid())){
                        idList.add(message.getIdFrom());
                    }
                }
                readUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readUsers() {
        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    User user = snapshot.getValue(User.class);
                    for ( String id : idList ){
                        if ( user.getId().equals(id) ){
                            if ( userList.size() == 0 ){
                                userList.add(user);
                            }else{
                                int count = 0;
                                for ( User user1 : userList){
                                    if (user1.getId().equals(id) )
                                        count++;
                                }
                                if ( count == 0)
                                    userList.add(user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter((MenuPrincipaleActivity) getActivity(),getContext() , userList,UserAdapter.SOURCE_MESSAGERIE);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
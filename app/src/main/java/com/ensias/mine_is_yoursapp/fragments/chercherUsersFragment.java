package com.ensias.mine_is_yoursapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ensias.mine_is_yoursapp.LoginActivity;
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


public class chercherUsersFragment extends Fragment {

    EditText editText;
    RecyclerView recyclerView;

    UserAdapter userAdapter;
    FirebaseUser firebaseUser;
    ArrayList<User> userList;

    DatabaseReference databaseReference;
    ValueEventListener listener;

    public chercherUsersFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( firebaseUser == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_chercher_users, container, false);
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       editText = view.findViewById(R.id.text_search);
       recyclerView = view.findViewById(R.id.recycler_view);

       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       userList = new ArrayList<>();
       userAdapter = new UserAdapter((MenuPrincipaleActivity) getActivity(),getContext() , userList,UserAdapter.SOURCE_SEARCH);
       recyclerView.setAdapter(userAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ( databaseReference != null && listener!= null)
                    databaseReference.removeEventListener(listener);
                if (!editText.getText().toString().equals("") )
                    readUsers(editText.getText().toString());
            }
        });






        return view;
    }
    private void readUsers(String regex) {
        userAdapter.renewItems(new ArrayList<>());

        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("users");
        listener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    User user = snapshot.getValue(User.class);
                    if ( user.getFirstName().contains(regex) || user.getLastName().contains(regex) || user.getEmail().contains(regex) || (user.getLastName()+" "+user.getFirstName()).contains(regex) || (user.getFirstName()+" "+user.getLastName()).contains(regex)  )
                        userAdapter.addItem(user);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(listener);

    }

}
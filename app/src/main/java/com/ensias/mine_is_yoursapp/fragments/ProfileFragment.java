package com.ensias.mine_is_yoursapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.ensias.mine_is_yoursapp.Control.SessionManager;
import com.ensias.mine_is_yoursapp.LoginActivity;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private DatabaseReference mDatabase;

    TextView username_profile, phone_profile, email_profile, add_profile;
    FrameLayout back ;
    Button edit_profile ;
    FirebaseUser user;
    User myUser ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username_profile = view.findViewById(R.id.username_profile);
        phone_profile = view.findViewById(R.id.phone_profile);
        email_profile = view.findViewById(R.id.email_profile);
        add_profile = view.findViewById(R.id.add_profile);

        edit_profile = view.findViewById(R.id.edit_profile);
        back = view.findViewById(R.id.back_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users").child(user.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {

/*
        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String,String> userLogin = sessionManager.getRememberMeSessionDetails();
            email_profile.setText(userLogin.get(SessionManager.KEY_SESSION_EMAIL));
        }
*/
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class) ;
                username_profile.setText(myUser.getFirstName()+" " + myUser.getLastName());
                email_profile.setText(myUser.getEmail());
                phone_profile.setText(myUser.getPhone());
                add_profile.setText(myUser.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(),"Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();
                UpdateProfileFragment fragment = new  UpdateProfileFragment(myUser) ;
                getFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });

        // Inflate the layout for this fragment
        return view ;
    }
}
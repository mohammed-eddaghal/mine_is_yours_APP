package com.ensias.mine_is_yoursapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilMenuFragment extends Fragment {


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ValueEventListener listener;

    CircleImageView imageView;
    TextView        username;

    LinearLayout linearLayout1;
    LinearLayout linearLayout2;

    Button deconnexion;


    public ProfilMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( databaseReference != null && listener != null )
                databaseReference.removeEventListener(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profil_menu, container, false);

        //Binding
        imageView = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.nom_complet);
        linearLayout1 = view.findViewById(R.id.btn_profil);
        linearLayout2 = view.findViewById(R.id.btn_outils);
        deconnexion = view.findViewById(R.id.deconnexion);



        LoadUserInfos();

        linearLayout1.setOnClickListener(e->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, new ProfileFragment()).commit();
        });
        linearLayout2.setOnClickListener(e->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, new MesOutilsFragment(firebaseUser.getUid())).commit();
        });



        return view;
    }

    private void LoadUserInfos() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                            .getReference("users")
                            .child(firebaseUser.getUid());
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if ( user != null){

                    username.setText(user.getLastName()+ " " + user.getFirstName());

                    if ( user.getImage().equals("default") || user.getImage() == null )
                        Glide.with(getContext()).load(R.drawable.no_image).into(imageView);
                    else
                        Glide.with(getContext()).load(user.getImage()).into(imageView);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        databaseReference.addValueEventListener(listener);

    }
}
package com.ensias.mine_is_yoursapp.fragments;

import android.media.tv.TvView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.MessageAdapter;
import com.ensias.mine_is_yoursapp.adapters.OutilAdapter;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherUserProfileFragment extends Fragment {


    private DatabaseReference databaseReference;
    ValueEventListener listener;
    private DatabaseReference databaseReference2;
    ValueEventListener listener2;
    private String idUser;
    private User user;

    TextView username;
    TextView email;
    CircleImageView profile_image;
    Button btn_contacter;

    RecyclerView recyclerView;
    ArrayList<Outil> listOutils;
    OutilAdapter outilAdapter;


    public OtherUserProfileFragment(String idUser) {
       this.idUser=idUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( databaseReference != null && listener != null)
            databaseReference.removeEventListener(listener);
        if ( databaseReference2 != null && listener2 != null)
            databaseReference2.removeEventListener(listener2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_other_user_profile, container, false);

        username        = view.findViewById(R.id.nom_complet);
        email           =  view.findViewById(R.id.email_profile);
        profile_image   = view.findViewById(R.id.profile_image);
        btn_contacter   =  view.findViewById(R.id.contacter_btn);

        recyclerView    = view.findViewById(R.id.recycler_view);

        //importer les infos du courant user
        importerInfoUser();
        //Liste des outils du courant user
        importerOutilsCourantUser();

        btn_contacter.setOnClickListener(e ->{
            ((MenuPrincipaleActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new MessagesUsersFragment(this,user)).commit();
        });

        //ajouter les outils
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        outilAdapter = new OutilAdapter((MenuPrincipaleActivity) getActivity(),getContext(), listOutils,this);
        recyclerView.setAdapter(outilAdapter);

        return view;
    }

    private void importerOutilsCourantUser() {
        listOutils = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("outils");
        listener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOutils.clear();

                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Outil outil = snapshot.getValue(Outil.class);
                    if( outil.getIdOwner().equals(idUser)){
                        Log.e("outil","enter");
                        outilAdapter.addItem(outil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(listener);
    }

    private void importerInfoUser() {
        databaseReference2 = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("users").child(idUser);
        listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                email.setText(user.getEmail());
                username.setText(user.getLastName() + " " + user.getFirstName() );
                if ( user.getImage().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(OtherUserProfileFragment.this).load(user.getImage()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference2.addValueEventListener(listener2);
    }
}
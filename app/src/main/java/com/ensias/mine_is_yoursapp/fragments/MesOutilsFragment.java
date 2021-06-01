package com.ensias.mine_is_yoursapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.OutilAdapter;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MesOutilsFragment extends Fragment {

    private String idUser;
    RecyclerView recyclerView;
    OutilAdapter outilAdapter;
    List<Outil> listOutils;

    DatabaseReference databaseReference;
    ValueEventListener listener;

    public MesOutilsFragment(String idUser) {
       this.idUser = idUser;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseReference != null && listener != null )
            databaseReference.removeEventListener(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mes_outils, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        LoadOutils();

        outilAdapter = new OutilAdapter((MenuPrincipaleActivity) getActivity(),getContext(), listOutils,this);
        recyclerView.setAdapter(outilAdapter);

        return view;
    }

    private void LoadOutils() {
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
}
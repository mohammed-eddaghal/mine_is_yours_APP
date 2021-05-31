package com.ensias.mine_is_yoursapp.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.SliderAdapter;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.SliderItem;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import de.hdodenhof.circleimageview.CircleImageView;

public class OutilDetailsFragment extends Fragment {

    private Outil outil;
    Fragment fragmentPrecedant;

    SliderView              sliderView;
    CircleImageView         circleImageView;
    TextView                nomOwner;
    TextView                etat_outil;
    TextView                titreOutil;
    TextView                categorieOutil;
    TextView                descriptionOutil;
    Button                  contacter;
    FloatingActionButton    return_btn;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    SliderAdapter sliderAdapter;
    OutilDetailsFragment instance =this;



    public OutilDetailsFragment(Outil outil , Fragment fragment) {
        this.outil              = outil;
        this.fragmentPrecedant  = fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( valueEventListener != null && databaseReference != null )
            databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outil_details, container, false);

        //Binding
        sliderView          = view.findViewById(R.id.imageSlider);
        circleImageView     = view.findViewById(R.id.profile_image);
        nomOwner            = view.findViewById(R.id.nom_owner);
        etat_outil          = view.findViewById(R.id.etat_outil);
        titreOutil          = view.findViewById(R.id.titre);
        categorieOutil      = view.findViewById(R.id.type);
        descriptionOutil    = view.findViewById(R.id.description);
        contacter           = view.findViewById(R.id.contacter_btn);
        return_btn          = view.findViewById(R.id.btn_return);




        //charger le titre , catégorie et la description
        titreOutil.setText(outil.getTitre());
        categorieOutil.setText(outil.getType());
        descriptionOutil.setText(outil.getDescription());

        //charger l'état  de l'outil changer la couleur du texte selon l'etat de l'outil
        if( outil.getEtat().equals("available") ) {
            etat_outil.setTextColor(Color.GREEN);
            etat_outil.setText("Outil disponible");
        }else {
            etat_outil.setTextColor(Color.RED);
            etat_outil.setText("Outil non disponible");
        }

        //importer les details de l'owner
        getInfosOwner();

        //Charger les images dans le sliderView
        sliderAdapter = new SliderAdapter(getContext());
        sliderAdapter.renewItems(SliderItem.getSliderItemsFromUrisItems(outil.getUris()));
        sliderView.setSliderAdapter(sliderAdapter);

        //retour au fragment precedant
        return_btn.setOnClickListener(e->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragmentPrecedant).commit();
        });

        //Ouvrir une discussion avec le propritaire du
        contacter.setOnClickListener(e->{
            if ( outil.getIdOwner() != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance()
                                                .getReference("users").child(outil.getIdOwner());

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User owner = dataSnapshot.getValue(User.class);
                        MessagesUsersFragment messagesUsersFragment = new MessagesUsersFragment(instance,owner);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_main_frame_layout, messagesUsersFragment).commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });


        return view;
    }

    private void getInfosOwner() {
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("users").child(outil.getIdOwner());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if ( user != null ){
                    //charger le nom du prprietaire de l'outil
                    nomOwner.setText(user.getLastName()+" "+ user.getFirstName());

                    //Charger l'image
                    if ( user.getImage().equals("default")){
                        Glide.with(getContext()).load(R.drawable.no_image).into(circleImageView);
                    }else{
                        Glide.with(getContext()).load(user.getImage()).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}
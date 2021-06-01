package com.ensias.mine_is_yoursapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

public class MonOutilDetailsFragment extends Fragment {

    private Outil outil;
    Fragment fragmentPrecedant;

    SliderView              sliderView;
    TextView                etat_outil;
    TextView                titreOutil;
    TextView                categorieOutil;
    TextView                descriptionOutil;
    Button                  editer;
    Button                  supprimer;
    FloatingActionButton    return_btn;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    SliderAdapter sliderAdapter;



    public MonOutilDetailsFragment(Outil outil , Fragment fragment) {
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
        View view = inflater.inflate(R.layout.fragment_mon_outil_details, container, false);

        //Binding
        sliderView          = view.findViewById(R.id.imageSlider);
        etat_outil          = view.findViewById(R.id.etat_outil);
        titreOutil          = view.findViewById(R.id.titre);
        categorieOutil      = view.findViewById(R.id.type);
        descriptionOutil    = view.findViewById(R.id.description);
        editer              = view.findViewById(R.id.editer_btn);
        supprimer           = view.findViewById(R.id.supprimer_btn);
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

        //editer l'outil
        editer.setOnClickListener(e-> {
            EditerOutilFragment editerOutilFragment = new EditerOutilFragment(outil,this);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, editerOutilFragment).commit();

        });
        //supprimer l'outil
        supprimer.setOnClickListener(e-> {
            supprimer.setEnabled(false);
            databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                    .getReference("outils").child(outil.getId());
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_main_frame_layout, fragmentPrecedant).commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addListenerForSingleValueEvent(valueEventListener);


        });

        return view;
    }


}
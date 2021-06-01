package com.ensias.mine_is_yoursapp.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.SliderAdapter;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.SliderItem;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditerOutilFragment extends Fragment {


    int PICK_IMAGE_MULTIPLE = 1;
    private Outil outil;
    Fragment fragmentPrecedant;

    SliderView              sliderView;

    FloatingActionButton    return_btn;
    FloatingActionButton         suppIcon;
    FloatingActionButton         addIcon;

    Switch aSwitch;

    TextView                etat_outil;
    TextView                titreOutil;
    Spinner                 categorieOutil;
    TextView                descriptionOutil;

    Button                  sauvegarder;
    Button                  supprimer;

    ArrayList<String> listUrisfirestorage;
    ArrayList<Uri> listImagesUri;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    SliderAdapter sliderAdapter;
    EditerOutilFragment instance =this;



    public EditerOutilFragment(Outil outil , Fragment fragment) {
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
        View view = inflater.inflate(R.layout.fragment_editer_outil, container, false);
        listImagesUri = new ArrayList<>();
        //Binding
        sliderView          = view.findViewById(R.id.imageSlider);

        addIcon             = view.findViewById(R.id.import_images);
        suppIcon            = view.findViewById(R.id.delete_image);
        return_btn          = view.findViewById(R.id.btn_return);

        aSwitch             = view.findViewById(R.id.switch2);
        titreOutil          = view.findViewById(R.id.titre);
        categorieOutil      = view.findViewById(R.id.type);
        descriptionOutil    = view.findViewById(R.id.description);

        supprimer         = view.findViewById(R.id.supprimer_btn);
        sauvegarder         = view.findViewById(R.id.btn_sauvegarder);


        //Remplir la liste des categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorie_outil, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorieOutil.setAdapter(adapter);
        String compareValue = outil.getType();
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            categorieOutil.setSelection(spinnerPosition);
        }

        //charger le titre , catégorie et la description
        titreOutil.setText(outil.getTitre());

        descriptionOutil.setText(outil.getDescription());

        //charger l'état  de l'outil changer la couleur du texte selon l'etat de l'outil
        if( outil.getEtat().equals("available") ) {
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }

        //ajouter un listenner pour le bouter envoyer
        sauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sauvegardeOutil();
            }
        });

        //Charger les images dans le sliderView
        sliderAdapter = new SliderAdapter(getContext());
        sliderAdapter.renewItems(SliderItem.getSliderItemsFromUrisItems(outil.getUris()));
        sliderView.setSliderAdapter(sliderAdapter);

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
                            .replace(R.id.activity_main_frame_layout, ((MenuPrincipaleActivity)getActivity()).getProfileFragment()).commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addListenerForSingleValueEvent(valueEventListener);


        });

        //retour au fragment precedant
        return_btn.setOnClickListener(e->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragmentPrecedant).commit();
        });

        //ajouter Listening à l'uploaderImage bouton
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImgs();
            }
        });
        suppIcon.setOnClickListener(e->{
            if ( sliderAdapter.getCount() != 0) {
                String url = sliderAdapter.getmSliderItems().get(sliderView.getCurrentPagePosition()).getUrl().toString();
                sliderAdapter.deleteItem(sliderView.getCurrentPagePosition());
                for (int i=0 ; i<outil.getUris().size() ;i++){
                    if ( outil.getUris().get(i).equals(url))
                        outil.getUris().remove(i);
                }
                for ( int j=0 ; j<listImagesUri.size();j++){
                    if ( listImagesUri.get(j).equals(sliderAdapter.getmSliderItems().get(sliderView.getCurrentPagePosition()).getUrl()))
                        listImagesUri.remove(listImagesUri.get(j));
                }
            }
        });


        return view;
    }

    private void sauvegardeOutil() {

        String titreS   = titreOutil.getText().toString();

        //Bon Titre
        if ( !titreS.equals("") ){
            if ( aSwitch.isChecked())
                    outil.setEtat("available");
            else
                    outil.setEtat("not available");
            outil.setDescription(descriptionOutil.getText().toString());
            outil.setTitre(titreS);
            outil.setType(categorieOutil.getSelectedItem().toString());

            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("Sauvegarde en cours ! ");
            pd.show();

            List<Task<Uri>> taskArrayList= new ArrayList<>();
            for ( Uri uri : listImagesUri ){
                taskArrayList.add(taskUpload(uri));
            }

            Tasks.whenAllSuccess(taskArrayList).addOnCompleteListener(task -> {
                pd.dismiss();
                databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                        .getReference("outils").child(outil.getId());
                databaseReference.setValue(outil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //renouvler un nv fragment ajouterOutilFragment
                        ((MenuPrincipaleActivity)getActivity()).setAjouterOutilFragment(new AjouterOutilFragment());
                        //apres le sauvegarde se termine avec succes !
                        getActivity().getSupportFragmentManager().beginTransaction().replace(
                                R.id.activity_main_frame_layout,fragmentPrecedant).commit();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getContext()  , "Une erreur est survenue, Réessayer ultérieurement",Toast.LENGTH_SHORT);
                            }
                        });
            });



        }else{
            Toast.makeText(getContext() , "Entrer un titre ! ",Toast.LENGTH_SHORT).show();
            titreOutil.getError();
        }
    }

    private String getFileExtension(Uri uri ){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImgs() {
        // initialising intent
        Intent intent = new Intent();

        // setting type to select to be image
        intent.setType("image/*");

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Ajouter" , "done");
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == getActivity().RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    listImagesUri.add(imageurl);
                    SliderItem sliderItem = new SliderItem(imageurl);
                    sliderAdapter.addItem(sliderItem);


                }
                // setting elements into the imageSlider TO-DO

            } else {
                Uri imageurl = data.getData();
                listImagesUri.add(imageurl);
                sliderAdapter.addItem(new SliderItem(imageurl));

            }
        } else {
            // show this if no image is selected
            Toast.makeText(getActivity(), "Aucune image sélectionnée !", Toast.LENGTH_LONG).show();
        }





    }
    private Task<Uri> taskUpload(Uri uri){
        final StorageReference fileReference = FirebaseStorage.getInstance("gs://mineisyours-68d08.appspot.com/")
                .getReference().child("outilsimages/"+System.currentTimeMillis()+"."+getFileExtension(uri));
        UploadTask uploadTask = fileReference.putFile(uri);
        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task ) throws  Exception   {
                if ( !task.isSuccessful())
                    throw task.getException();
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if ( task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String mUri =downloadUri.toString();
                    outil.getUris().add(mUri);


                }else{
                    Toast.makeText(getContext(), "Failed" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext() , e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }





}
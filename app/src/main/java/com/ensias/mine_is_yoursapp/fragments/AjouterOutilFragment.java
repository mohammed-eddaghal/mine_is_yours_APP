package com.ensias.mine_is_yoursapp.fragments;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.SliderAdapter;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.ensias.mine_is_yoursapp.model.SliderItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class AjouterOutilFragment extends Fragment {

    int PICK_IMAGE_MULTIPLE = 1;

    SliderView sliderView;
    EditText description;
    EditText titre;
    Spinner type;
    Button ajouterOutil;
    FloatingActionButton importerImgs;

    SliderAdapter sliderAdapter;

    private Outil outil;

    FirebaseUser firebaseUser;
    ArrayList<String> listUrisfirestorage;
    ArrayList<Uri> listImagesUri;

    DatabaseReference databaseReference;

    public AjouterOutilFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_ajouter_outil, container, false);

       //binding
       description          = view.findViewById(R.id.description);
       titre                = view.findViewById(R.id.titre);
       type                 = view.findViewById(R.id.type);
       ajouterOutil         = view.findViewById(R.id.btn_ajouter);
       importerImgs         = view.findViewById(R.id.import_images);
       sliderView           = view.findViewById(R.id.imageSlider);
       firebaseUser         = FirebaseAuth.getInstance().getCurrentUser();

       listImagesUri        = new ArrayList<>();
       listUrisfirestorage = new ArrayList<>();

       //Slider to show Images
       sliderAdapter =new SliderAdapter(getContext());
       sliderView.setSliderAdapter(sliderAdapter);


       //Remplir la liste des categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorie_outil, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);

       //ajouter Listening à l'uploaderImage bouton
        importerImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImgs();
            }
        });

        //ajouter un listenner pour le bouter envoyer
        ajouterOutil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sauvegardeOutil();
            }
        });


       return view;
    }
    private String getFileExtension(Uri uri ){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    // Sauvegarder l'outil
    private void sauvegardeOutil() {
        String descrip = description.getText().toString();
        String titreS   = titre.getText().toString();
        String typeS = type.getSelectedItem().toString();

        outil = new Outil(firebaseUser.getUid(),typeS,titreS,descrip,listUrisfirestorage);
        //Bon Titre
        if ( !titreS.equals("") ){
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("Sauvegarde en cours ! ");
            pd.show();
            final int[] toCatchAfailure = {0};
            List<Task<Uri>> taskArrayList= new ArrayList<>();
            for ( Uri uri : listImagesUri ){

                taskArrayList.add(taskUpload(uri));

            }
            Tasks.whenAllSuccess(taskArrayList).addOnCompleteListener(task -> {
                pd.dismiss();
                String idOutil = String.valueOf(System.currentTimeMillis());
                outil.setId(idOutil);
                databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                        .getReference("outils").child(idOutil);
                Log.e("test",outil + " " +outil.getUris().size());

                databaseReference.setValue(outil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //renouvler un nv fragment ajouterOutilFragment
                        ((MenuPrincipaleActivity)getActivity()).setAjouterOutilFragment(new AjouterOutilFragment());
                        //apres le sauvegarde se termine avec succes !
                        getActivity().getSupportFragmentManager().beginTransaction().replace(
                                R.id.activity_main_frame_layout,( (MenuPrincipaleActivity) getActivity())
                                        .getBoiteMessagerieFragment()).commit();

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
           titre.getError();
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
                    sliderAdapter.addItem(new SliderItem(imageurl));
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

        Log.i("Ajouter" , "done");
        //Affichage des uris ( à enlever )
        for ( Uri uri : listImagesUri)
            Log.i("Ajouter" , uri.toString());
    }
}
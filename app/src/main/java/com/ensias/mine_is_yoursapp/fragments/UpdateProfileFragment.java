package com.ensias.mine_is_yoursapp.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.SliderAdapter;
import com.ensias.mine_is_yoursapp.model.SliderItem;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileFragment extends Fragment {

    int PICK_IMAGE_MULTIPLE = 1;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    public Uri ImageUri = null;
    String imageUrl ;

    EditText firstname_profile, lastname_profile, phone_profile, email_profile, add_profile, lantitude, langitude;
    TextView nom_user ;
    Button annule_profile, update_profile ;
    FloatingActionButton photo;
    CircleImageView myImage ;

    User user ;

    public UpdateProfileFragment(User user) {
        this.user = user ;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Update","Attached");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        myImage = view.findViewById(R.id.photo);
        nom_user = view.findViewById(R.id.nom_user);

        firstname_profile = view.findViewById(R.id.firstname_profile);
        lastname_profile = view.findViewById(R.id.lastname_profile);
        phone_profile = view.findViewById(R.id.phone_profile);
        email_profile = view.findViewById(R.id.email_profile);
        add_profile = view.findViewById(R.id.add_profile);
        langitude = view.findViewById(R.id.langitude);
        lantitude = view.findViewById(R.id.lantitude);

        update_profile = view.findViewById(R.id.update_profile);
        annule_profile = view.findViewById(R.id.annule_profile);
        photo = view.findViewById(R.id.editPhoto);

        imageUrl = user.getImage();
        if ( !user.getImage().equals("default"))
            Glide.with(getContext()).load(user.getImage()).into(myImage);

        firstname_profile.setText(user.getFirstName());
        lastname_profile.setText(user.getLastName());
        phone_profile.setText(user.getPhone());
        email_profile.setText(user.getEmail());
        add_profile.setText(user.getAddress());
        lantitude.setText(user.getLantitude().toString());
        langitude.setText(user.getLangitude().toString());

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImgs();
            }
        });

        ClickUpdate();

        annule_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new  ProfileFragment() ;
                getFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });

        return view;
    }

    private void ClickUpdate() {

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lastname_profile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (firstname_profile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (phone_profile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (email_profile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (add_profile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (langitude.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else if (lantitude.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Et hop ! un message à l'écran :D", Toast.LENGTH_LONG).show();

                } else {


                    imageUpload(ImageUri);

                }
            }
        });
    }

    private void uploadImgs() {
        // initialising intent
        Intent intent = new Intent();

        // setting type to select to be image
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == getActivity().RESULT_OK && null != data) {

            // Get the Image from data
            ImageUri = data.getData();
            myImage.setImageURI(ImageUri);

        } else {
            // show this if no image is selected
            Toast.makeText(getActivity(), "Aucune image sélectionnée !", Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri uri ){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void imageUpload(Uri uri){
        mDatabase = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("users").child(user.getId());

        if (ImageUri !=null && !ImageUri.toString().equals(user.getImage()) ){

            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Uploading image...");
            pd.show();

            final StorageReference fileReference = FirebaseStorage.getInstance("gs://mineisyours-68d08.appspot.com/")
                    .getReference().child("usersprofile/"+System.currentTimeMillis()+"."+getFileExtension(uri));

            UploadTask uploadTask = fileReference.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        pd.dismiss();
                        Uri downloadUri = task.getResult();
                        imageUrl =downloadUri.toString();

                        user.setImage(imageUrl);

                        user.setFirstName(firstname_profile.getText().toString());
                        user.setLastName(lastname_profile.getText().toString());
                        user.setPhone(phone_profile.getText().toString());
                        user.setEmail(email_profile.getText().toString());
                        user.setAddress(add_profile.getText().toString());
                        user.setLangitude(Double.parseDouble(langitude.getText().toString() ));
                        user.setLantitude(Double.parseDouble(lantitude.getText().toString() ));

                        mDatabase.setValue(user);

                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_main_frame_layout,((MenuPrincipaleActivity) getActivity()).getProfileFragment())
                                .commit();
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

        }else{

            user.setFirstName(firstname_profile.getText().toString());
            user.setLastName(lastname_profile.getText().toString());
            user.setPhone(phone_profile.getText().toString());
            user.setEmail(email_profile.getText().toString());
            user.setAddress(add_profile.getText().toString());

            mDatabase.setValue(user);
            ProfileFragment fragment = new  ProfileFragment() ;
            getFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }
}
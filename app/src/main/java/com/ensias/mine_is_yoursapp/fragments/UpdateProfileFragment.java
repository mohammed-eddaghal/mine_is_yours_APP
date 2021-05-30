package com.ensias.mine_is_yoursapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileFragment extends Fragment {

    int PICK_IMAGE_MULTIPLE = 1;
    private DatabaseReference mDatabase;

    TextView firstname_profile, lastname_profile, phone_profile, email_profile, add_profile;
    Button annule_profile, update_profile , photo;
    User user ;

    public UpdateProfileFragment(User user) {
        this.user = user ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        firstname_profile = view.findViewById(R.id.firstname_profile);
        lastname_profile = view.findViewById(R.id.lastname_profile);
        phone_profile = view.findViewById(R.id.phone_profile);
        email_profile = view.findViewById(R.id.email_profile);
        add_profile = view.findViewById(R.id.add_profile);

        update_profile = view.findViewById(R.id.update_profile);
        annule_profile = view.findViewById(R.id.annule_profile);
        photo = view.findViewById(R.id.photo);

        firstname_profile.setText(user.getFirstName());
        lastname_profile.setText(user.getLastName());
        phone_profile.setText(user.getPhone());
        email_profile.setText(user.getEmail());
        add_profile.setText(user.getAddress());

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

                } else {
                    mDatabase = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users").child(user.getId());

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
}
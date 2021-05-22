package com.ensias.mine_is_yoursapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.model.Message;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextView username_profile, phone_profile, email_profile, add_profile;
    FrameLayout back ;
    Button edit_profile ;

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
        back =  view.findViewById(R.id.back_profile);
/*
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //username_profile.setText("fouad el fakhori");
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // username_profile.setText("fouad el fakhori");
            }
        });
*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
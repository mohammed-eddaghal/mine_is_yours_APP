package com.ensias.mine_is_yoursapp.fragments;

import android.app.AppComponentFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.MenuPrincipaleActivity;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.adapters.MessageAdapter;
import com.ensias.mine_is_yoursapp.model.Message;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesUsersFragment extends Fragment {

    private FirebaseUser userFirebase;
    private DatabaseReference databaseReference;
    private User user;

    CircleImageView profile_image;
    TextView username;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Message> messagesList;
    RecyclerView recyclerView;



    public MessagesUsersFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_users, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                BoiteMessagerieFragment fragment = ((MenuPrincipaleActivity)getActivity()).getBoiteMessagerieFragment();
                 getFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        btn_send = view.findViewById(R.id.btn_send);
        text_send = view.findViewById(R.id.text_send);

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if ( !text_send.getText().toString().equals("")  ) {
                    Message message = new Message(userFirebase.getUid(), user.getId(), text_send.getText().toString());
                    sendMessage(message);
                }else{
                    Toast.makeText(getContext() ,"Vous ne pouvez pas envoyer un message vide !",Toast.LENGTH_SHORT );
                }
                text_send.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/").getReference("users").child(user.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                username.setText(user.getFirstName()+" "+user.getLastName());
                if ( user.getImage().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MessagesUsersFragment.this).load(user.getImage()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        readMessages(userFirebase.getUid(),user.getId(),user.getImage());
        messageAdapter = new MessageAdapter(getContext() , messagesList,user.getImage());
        recyclerView.setAdapter(messageAdapter);

        return view;
    }
    private void sendMessage(Message message){
         DatabaseReference reference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                 .getReference();
         reference.child("messages").push().setValue(message);
    }
    private  void readMessages(final String myid , final String userId , final String imageurl){
        messagesList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/")
                .getReference("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //messagesList.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getIdTo().equals(myid) && message.getIdFrom().equals(userId) ||
                            message.getIdTo().equals(userId) && message.getIdFrom().equals(myid)) {
                        messageAdapter.addItem(message);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
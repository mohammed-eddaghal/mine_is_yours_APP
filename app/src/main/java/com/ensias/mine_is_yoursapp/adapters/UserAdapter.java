package com.ensias.mine_is_yoursapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.fragments.MessagesUsersFragment;
import com.ensias.mine_is_yoursapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> listUsers;

    public UserAdapter(Context context , List<User> listUsers){
        this.context =context;
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent , false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = listUsers.get(position);
        holder.username.setText(user.getFirstName() + " "+user.getLastName());
        if ( user.getImage().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        }else{
            Glide.with(context).load(user.getImage()).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new Fragment
                MessagesUsersFragment messageFragment =new MessagesUsersFragment(user);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, messageFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.image_user);
        }
    }


}

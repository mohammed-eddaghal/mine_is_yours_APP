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
import com.ensias.mine_is_yoursapp.model.Message;
import com.ensias.mine_is_yoursapp.model.SliderItem;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT  =    0;
    public static final int MSG_TYPE_RIGHT =    1;

    private Context context;
    private List<Message> listMessage;
    private String imageurl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context , List<Message> listMessage,String imageur){
        this.context =context;
        this.listMessage = listMessage;
        this.imageurl=imageur;
    }
    public void deleteItem(int position) {
        this.listMessage.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Message message) {
        this.listMessage.add(message);
        notifyDataSetChanged();
    }
    public void renewItems(List<Message> listMessage) {
        this.listMessage = listMessage;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if ( viewType == MSG_TYPE_RIGHT){
           View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent , false);
           return new MessageAdapter.ViewHolder(view);
       }else{
           View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent , false);
           return new MessageAdapter.ViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = listMessage.get(position);
        holder.show_message.setText(message.getText());
        if ( imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageurl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( listMessage.get(position).getIdFrom().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

package com.ensias.mine_is_yoursapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ensias.mine_is_yoursapp.R;
import com.ensias.mine_is_yoursapp.model.Message;
import com.ensias.mine_is_yoursapp.model.Outil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class OutilAdapter extends RecyclerView.Adapter<OutilAdapter.ViewHolder> {
    public static final int OUTIL_AVAILABLE         =    0;
    public static final int OUTIL_NOT_AVAILABLE     =    1;

    private Context         context;
    private List<Outil>     listOutils;

    FirebaseUser firebaseUser;

    public OutilAdapter(Context context , List<Outil> listOutils){
        this.context =context;
        this.listOutils = listOutils;
    }
    public void deleteItem(int position) {
        this.listOutils.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Outil outil) {
        this.listOutils.add(outil);
        notifyDataSetChanged();
    }
    public void renewItems(List<Outil> listOutils) {
        this.listOutils = listOutils;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if ( viewType == OUTIL_AVAILABLE){
           View view = LayoutInflater.from(context).inflate(R.layout.outil_item_disponible, parent , false);
           return new OutilAdapter.ViewHolder(view);
       }else{
           View view = LayoutInflater.from(context).inflate(R.layout.outil_item_non_disponible, parent , false);
           return new OutilAdapter.ViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Outil outil = listOutils.get(position);
        holder.titre_outil.setText(outil.getTitre());
        holder.description_outil.setText(outil.getDescription());
        if ( outil.getUris().size() == 0 ){
            holder.image_outil.setImageResource(R.drawable.no_image);
        }else{
            Glide.with(context).load(outil.getUris().get(0)).into(holder.image_outil);
        }

    }

    @Override
    public int getItemCount() {
        return listOutils.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView titre_outil;
        public TextView description_outil;
        public ImageView image_outil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titre_outil = itemView.findViewById(R.id.titre_outil);
            description_outil = itemView.findViewById(R.id.description_outil);
            image_outil = itemView.findViewById(R.id.image_outil);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( listOutils.get(position).getEtat().equals("available")){
            return OUTIL_AVAILABLE;
        }else{
            return OUTIL_NOT_AVAILABLE;
        }
    }
}

package com.dell.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<Media> songs;
    public RecyclerViewAdapter(Context context,List<Media> songs) {
        this.context = context;
        this.songs = songs;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.item_view,parent,false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.artist.setText(songs.get(position).getArtist());
        holder.title.setText(songs.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
         TextView artist;
         TextView title;
         ImageView image;

         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artist =itemView.findViewById(R.id.artist);
            image =itemView.findViewById(R.id.image);
            title =itemView.findViewById(R.id.title);
        }
    }
}

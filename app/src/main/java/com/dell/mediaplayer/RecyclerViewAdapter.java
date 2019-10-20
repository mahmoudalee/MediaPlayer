package com.dell.mediaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Media> mSongs;
    public RecyclerViewAdapter(Context mContext,List<Media> mSongs) {
        this.mContext = mContext;
        this.mSongs = mSongs;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.item_view,parent,false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mArtist.setText(mSongs.get(position).getArtist());
        holder.mTitle.setText(mSongs.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
         TextView mArtist;
         TextView mTitle;
         ImageView mImage;

         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mArtist=itemView.findViewById(R.id.artist);
            mImage=itemView.findViewById(R.id.image);
            mTitle=itemView.findViewById(R.id.title);
        }
    }
}

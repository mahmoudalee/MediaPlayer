package com.dell.mediaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dell.mediaplayer.db.Song;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Song> songs;

//    Replaced with setData()

//    public RecyclerViewAdapter(Context context,List<Song> songs) {
//        this.context = context;
//        this.songs = songs;
//
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // to get context without pass it constructor
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_view, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.artist.setText(songs.get(position).getArtist());
        holder.title.setText(songs.get(position).getTitle());
        holder.duration.setText(songs.get(position).getDuration());
        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.optionMenu);
                menu.inflate(R.menu.menu_options);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
                menu.show();
            }
        });
        try {
            byte[] imageByte = songs.get(position).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            holder.image.setImageBitmap(bitmap);
        } catch (NullPointerException ex) {

        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.image);
                menu.inflate(R.menu.image_options);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
                menu.show();
            }
        });
    }

    // added condition to not crash if there's no data passed in first time
    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    public void setData(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView artist;
        TextView title;
        TextView duration;
        ImageView image;
        ImageButton optionMenu;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.artist);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            optionMenu = itemView.findViewById(R.id.option_menu);
        }
    }
}

package com.dell.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    List<Media> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mSongs=new ArrayList<>();

         /*
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
          */

         //setting up recycler view with adapter
        RecyclerView recycler=findViewById(R.id.recycler);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,mSongs);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        //adding line divider between item views
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getResources().getDrawable(R.drawable.divider));
        recycler.addItemDecoration(itemDecor);

    }
}

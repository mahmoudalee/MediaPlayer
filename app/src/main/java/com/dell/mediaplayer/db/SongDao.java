package com.dell.mediaplayer.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Insert
     void addSong(Song song);

    @Query("select * from Song")
    List<Song> viewAllSongs();
}

package com.dell.mediaplayer.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class},version = 1,exportSchema = false)
public abstract class SongDatabase extends RoomDatabase {
    public static SongDatabase getDatabase(Context context){
        Builder<SongDatabase> builder = Room.databaseBuilder(context, SongDatabase.class, "song.db");
        return builder.build();
    }


    public abstract SongDao getDaoInstance();

}

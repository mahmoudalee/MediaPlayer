package com.dell.mediaplayer.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo
    private String link;
    @ColumnInfo
    private String date;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String artist;

    public Song() {
    }

    public Song(String link, String date, String title, String artist ) {
        this.link = link;
        this.date = date;
        this.title = title;
        this.artist = artist;
    }

    public Song(int _id, String link, String date, String title, String artist) {
        this._id = _id;
        this.link = link;
        this.date = date;
        this.title = title;
        this.artist = artist;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}

package com.dell.mediaplayer.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    private String url;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @ColumnInfo
    private String duration;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @ColumnInfo
    private String artist;
    @ColumnInfo
    private byte[] image;


    public Song(String path, String date, String title, String artist,byte[] image,String url,String duration ) {
        this.link = path;
        this.date = date;
        this.title = title;
        this.artist = artist;
        this.image=image;
        this.url=url;
        this.duration=duration;
    }

    public Song(int _id, String link, String date, String title, String artist) {
        this._id = _id;
        this.link = link;
        this.date = date;
        this.title = title;
        this.artist = artist;
    }

     int get_id() {
        return _id;
    }

     void set_id(int _id) {
        this._id = _id;
    }

     String getLink() {
        return link;
    }

     void setLink(String link) {
        this.link = link;
    }

     String getDate() {
        return date;
    }

     void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

     void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

     void setArtist(String artist) {
        this.artist = artist;
    }
}

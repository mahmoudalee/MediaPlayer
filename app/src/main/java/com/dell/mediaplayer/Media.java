package com.dell.mediaplayer;

public class Media {
    private String title, artist;
    private Byte[] image;

    public Media(String title, String artist) {
        this.title = title;
        this.artist = artist;
      //  this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String mArtist) {
        this.artist = mArtist;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] mImage) {
        this.image = mImage;
    }


}

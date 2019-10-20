package com.dell.mediaplayer;

public class Media {
    private String mTitle,mArtist;
    private Byte[] mImage;

    public Media(String mTitle, String mArtist) {
        this.mTitle = mTitle;
        this.mArtist = mArtist;
      //  this.mImage = mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public Byte[] getImage() {
        return mImage;
    }

    public void setImage(Byte[] mImage) {
        this.mImage = mImage;
    }


}

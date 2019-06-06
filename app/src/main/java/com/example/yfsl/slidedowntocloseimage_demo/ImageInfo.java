package com.example.yfsl.slidedowntocloseimage_demo;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageInfo implements Parcelable {
    private int LEFT;
    private int TOP;
    private int WIDTH;
    private int HEIGHT;
    private int id;
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ImageInfo() {
    }

    protected ImageInfo(Parcel in) {
        LEFT = in.readInt();
        TOP = in.readInt();
        WIDTH = in.readInt();
        HEIGHT = in.readInt();
        id = in.readInt();
        imageId = in.readInt();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public int getLEFT() {
        return LEFT;
    }

    public void setLEFT(int LEFT) {
        this.LEFT = LEFT;
    }

    public int getTOP() {
        return TOP;
    }

    public void setTOP(int TOP) {
        this.TOP = TOP;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LEFT);
        dest.writeInt(TOP);
        dest.writeInt(WIDTH);
        dest.writeInt(HEIGHT);
        dest.writeInt(id);
        dest.writeInt(imageId);
    }
}

package com.promperu.pisco.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ScreenItem implements Parcelable {

    private String description, onboard, color;
    private int screenImg, position;

    public ScreenItem(String description, int screenImg, int position, String onboard) {
        this.description = description;
        this.screenImg = screenImg;
        this.onboard = onboard;
        this.position = position;
    }

    public ScreenItem(String description, int screenImg, int position, String onboard, String color) {
        this.description = description;
        this.screenImg = screenImg;
        this.onboard = onboard;
        this.position = position;
        this.color = color;
    }

    protected ScreenItem(Parcel in) {
        description = in.readString();
        onboard = in.readString();
        color = in.readString();
        screenImg = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(onboard);
        dest.writeString(color);
        dest.writeInt(screenImg);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScreenItem> CREATOR = new Creator<ScreenItem>() {
        @Override
        public ScreenItem createFromParcel(Parcel in) {
            return new ScreenItem(in);
        }

        @Override
        public ScreenItem[] newArray(int size) {
            return new ScreenItem[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public int getScreenImg() {
        return screenImg;
    }

    public String getOnboard() {
        return onboard;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
package com.example.luigidarco.myfit.models;

import android.graphics.Bitmap;

public class Workout {

    private int imageResource;
    private String title;
    private int time;

    public Workout(int image, String title, int time) {
        this.imageResource = image;
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "imageResource=" + imageResource +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}

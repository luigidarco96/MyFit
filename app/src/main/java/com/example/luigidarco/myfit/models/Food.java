package com.example.luigidarco.myfit.models;

import android.graphics.Bitmap;

public class Food extends Object{

    private Bitmap image;
    private String name;
    private int calorie;

    public Food(Bitmap image, String name, int calorie) {
        this.image = image;
        this.name = name;
        this.calorie = calorie;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    @Override
    public String toString() {
        return "Food{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", calorie=" + calorie +
                '}';
    }
}

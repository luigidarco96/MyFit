package com.example.luigidarco.myfit.models;

public class Food {

    private String imagePath;
    private String name;
    private int calorie;

    public Food() {}

    public Food(String imagePath, String name, int calorie) {
        this.imagePath = imagePath;
        this.name = name;
        this.calorie = calorie;
    }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) {this.imagePath = imagePath; }

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
                "imagePath=" + imagePath +
                ", name='" + name + '\'' +
                ", calorie=" + calorie +
                '}';
    }
}

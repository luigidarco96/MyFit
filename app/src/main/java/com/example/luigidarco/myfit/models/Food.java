package com.example.luigidarco.myfit.models;

public class Food {

    private int id;
    private String imagePath;
    private String name;
    private int calorie;
    private String date;

    public Food() {}

    public Food(String imagePath, String name, int calorie) {
        this.imagePath = imagePath;
        this.name = name;
        this.calorie = calorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", imagePath='" + imagePath + '\'' +
                ", name='" + name + '\'' +
                ", calorie=" + calorie +
                ", date='" + date + '\'' +
                '}';
    }
}

package com.example.luigidarco.myfit.models;

public class Workout {

    private int id;
    private int imageResource;
    private String title;
    private int time;
    private String date;

    public Workout() {}

    public Workout(int image, String title, int time) {
        this.imageResource = image;
        this.title = title;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", imageResource=" + imageResource +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", date='" + date + '\'' +
                '}';
    }
}

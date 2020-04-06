package com.example.luigidarco.myfit.models;

public class User {

    private int id;
    private String username;
    private String image_path;

    public User(int id, String username, String image_path) {
        this.id = id;
        this.username = username;
        this.image_path = image_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }
}

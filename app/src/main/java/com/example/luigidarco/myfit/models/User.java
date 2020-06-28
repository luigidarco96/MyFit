package com.example.luigidarco.myfit.models;

public class User {

    private int id;
    private String username;
    private String fullName;
    private int weight;
    private int height;
    private String dateBirth;
    private int gender;

    public User() {}

    public User(int id, String username, String fullName, int weight, int height, String dateBirth, int gender) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.weight = weight;
        this.height = height;
        this.dateBirth = dateBirth;
        this.gender = gender;
    }

    public boolean isNew() {
        if (this.fullName.equals("")) {
            return true;
        } else {
            return false;
        }
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", dateBirth='" + dateBirth + '\'' +
                ", gender=" + gender +
                '}';
    }
}

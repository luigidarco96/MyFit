package com.example.luigidarco.myfit.models;

import java.util.Date;

public class PersonalInfo {

    private int id;
    private double weight;
    private double height;
    private double bmi;
    private String bmiClass;
    private Date timestamp;

    public PersonalInfo(int id, double weight, double height, double bmi, String bmiClass, Date timestamp) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.bmiClass = bmiClass;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getBmiClass() {
        return bmiClass;
    }

    public void setBmiClass(String bmiClass) {
        this.bmiClass = bmiClass;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

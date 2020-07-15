package com.example.luigidarco.myfit.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private int id;
    private String username;
    private String fullName;
    private Date dateBirth;
    private int gender;
    private final static SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public User() {}

    public User(int id, String username, String fullName, String dateBirth, int gender) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        try {
            this.dateBirth = dataFormatter.parse(dateBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public String getDateBirth() {
        return dataFormatter.format(dateBirth);
    }

    public String getDateBirthSQL() {
        return new SimpleDateFormat("yyyy-MM-dd").format(dateBirth);
    }

    public void setDateBirth(String dateBirth) {
        try {
            this.dateBirth = dataFormatter.parse(dateBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                ", dateBirth='" + dateBirth + '\'' +
                ", gender=" + gender +
                '}';
    }
}

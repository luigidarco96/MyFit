package com.example.luigidarco.myfit.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.models.Workout;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class StorageManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String username = "username";
    private String accessToken = "access_token";
    private String refreshToken = "refresh_token";
    private String breakfastFood = "breakfast_food";
    private String lunchFood = "lunch_food";
    private String dinnerFood = "dinner_food";
    private String workout = "workout";

    public enum FoodsList {
        BREAKFAST,
        LUNCH,
        DINNER
    }

    public StorageManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getUsername() {
        return sharedPreferences.getString(this.username, "");
    }

    public void setUsername(String username) {
        editor.putString(this.username, username);
        editor.commit();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(this.accessToken, "");
    }

    public void setAccessToken(String accessToken) {
        editor.putString(this.accessToken, accessToken);
        editor.commit();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(this.refreshToken, "");
    }

    public void setRefreshToken(String refreshToken) {
        editor.putString(this.refreshToken, refreshToken);
        editor.commit();
    }

    public ArrayList<Food> getFoodsList(FoodsList typology) {
        Gson gson = new Gson();
        ArrayList<String> objString;

        switch (typology) {
            case BREAKFAST: objString = getListString(breakfastFood); break;
            case LUNCH: objString = getListString(lunchFood); break;
            case DINNER: objString = getListString(dinnerFood); break;
            default: objString = new ArrayList<>();
        }

        ArrayList<Food> foods =  new ArrayList<>();

        for (String jString: objString) {
            Food food = gson.fromJson(jString, Food.class);
            foods.add(food);
        }

        return foods;
    }

    public void setFoodsList(FoodsList typology, ArrayList<Food> foods) {
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for(Food obj : foods){
            objStrings.add(gson.toJson(obj));
        }
        switch (typology) {
            case BREAKFAST: putListString(breakfastFood, objStrings); break;
            case LUNCH: putListString(lunchFood, objStrings); break;
            case DINNER: putListString(dinnerFood, objStrings); break;
        }
    }

    public void addElementToFoodsList(FoodsList typology, Food food) {
        ArrayList<Food> foods = getFoodsList(typology);
        foods.add(food);
        setFoodsList(typology, foods);
    }

    public ArrayList<Workout> getWorkoutList() {
        Gson gson = new Gson();
        ArrayList<String> objString;
        objString = getListString(workout);

        ArrayList<Workout> workouts = new ArrayList<>();

        for (String jString: objString) {
            Workout workout = gson.fromJson(jString, Workout.class);
            workouts.add(workout);
        }

        return workouts;
    }

    public void setWorkoutList(ArrayList<Workout> workouts) {
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for(Workout obj : workouts){
            objStrings.add(gson.toJson(obj));
        }
        putListString(workout, objStrings);
    }

    public void addElementToWorkoutList(Workout workout) {
        ArrayList<Workout> workouts = getWorkoutList();
        workouts.add(workout);
        setWorkoutList(workouts);
    }

    public void deleteWorkoutList() {
        editor.remove(workout);
        editor.commit();
    }

    private ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedPreferences.getString(key, ""), "‚‗‚")));
    }

    private void putListString(String key, ArrayList<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        editor.putString(key, TextUtils.join("‚‗‚", myStringList));
        editor.commit();
    }

}

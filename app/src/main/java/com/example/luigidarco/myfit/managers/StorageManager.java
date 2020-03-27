package com.example.luigidarco.myfit.managers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.models.Workout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StorageManager {

    private final String TAG = "MYFITAPP";

    private Context mContext;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String myDevice = "my_device";
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
        this.mContext = context;
    }

    public BluetoothDevice getDevice() {
        Gson gson = new Gson();
        String device = sharedPreferences.getString(myDevice, "");
        if (device.equals("")) {
            return null;
        } else {
            return gson.fromJson(device, BluetoothDevice.class);
        }
    }

    public void setDevice(BluetoothDevice bluetoothDevice) {
        Gson gson = new Gson();
        String device = gson.toJson(bluetoothDevice);
        editor.putString(myDevice, device);
        editor.commit();
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

    public void deleteAccessToken() {
        editor.remove(accessToken);
        editor.commit();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(this.refreshToken, "");
    }

    public void setRefreshToken(String refreshToken) {
        editor.putString(this.refreshToken, refreshToken);
        editor.commit();
    }

    public void deleteRefreshToken() {
        editor.remove(refreshToken);
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

        String url = mContext.getResources().getString(R.string.url_server) + "/foods";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", food.getName());
            jsonObject.put("datetime", "2020-10-10");
            jsonObject.put("calorie", food.getCalorie());
            jsonObject.put("image_path", food.getImagePath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.makePostJsonObjRequest(mContext, url, jsonObject, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Log.d(TAG, object.toString());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        });
    }

    public void deleteElementFromFoodsList(FoodsList typology, int pos) {
        ArrayList<Food> foods = getFoodsList(typology);
        foods.remove(pos);
        setFoodsList(typology, foods);
    }

    public void deleteFoodsList(FoodsList foodsList) {
        switch (foodsList) {
            case BREAKFAST: editor.remove(breakfastFood); break;
            case LUNCH: editor.remove(lunchFood); break;
            case DINNER: editor.remove(dinnerFood); break;
        }
        editor.commit();
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
        String url = mContext.getResources().getString(R.string.url_server) + "/activities";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", workout.getTitle());
            jsonObject.put("datetime", "2020-10-10");
            jsonObject.put("duration", workout.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.makePostJsonObjRequest(mContext, url, jsonObject, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Log.d(TAG, object.toString());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        });
    }

    public void deleteElementFromWorkoutList(int pos) {
        ArrayList<Workout> workouts = getWorkoutList();
        workouts.remove(pos);
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

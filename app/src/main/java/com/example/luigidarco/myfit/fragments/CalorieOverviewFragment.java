package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.models.Workout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalorieOverviewFragment extends Fragment {

    StorageManager storageManager;
    ArrayList<Food> foods;

    TextView totalCalorie;
    TextView averageCalorie;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calorie_overview, container, false);

        storageManager = new StorageManager(getContext());

        totalCalorie = view.findViewById(R.id.calorie_overview_total);
        averageCalorie = view.findViewById(R.id.calorie_overview_average);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        foods = storageManager.getFoodsList(StorageManager.FoodsList.BREAKFAST);
        foods.addAll(storageManager.getFoodsList(StorageManager.FoodsList.LUNCH));
        foods.addAll(storageManager.getFoodsList(StorageManager.FoodsList.DINNER));

        totalCalorie.setText(getTotalCalorie(foods) + " cal");
        averageCalorie.setText(getAverage(foods) + " cal");
    }

    private int getTotalCalorie(ArrayList<Food> foods) {
        int tot = 0;
        for(Food f: foods) {
            tot += f.getCalorie();
        }
        return tot;
    }

    private int getAverage(ArrayList<Food> foods) {
        if (foods.isEmpty()) return 0;
        int tot = getTotalCalorie(foods);
        return tot / foods.size();
    }
}

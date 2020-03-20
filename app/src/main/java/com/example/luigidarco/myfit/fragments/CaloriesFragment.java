package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.utilities.StorageManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class CaloriesFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;

    private TextView totCalories;
    private View breakfast;
    private View lunch;
    private View dinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        totCalories = view.findViewById(R.id.total_calories);
        breakfast = view.findViewById(R.id.breakfast);
        lunch = view.findViewById(R.id.lunch);
        dinner = view.findViewById(R.id.dinner);

        breakfast.setOnClickListener(this.handleBreakfastClick);
        lunch.setOnClickListener(this.handleLunchClick);
        dinner.setOnClickListener(this.handleDinnerClick);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        int totCalories = 0;

        ArrayList<Food> totArray = storageManager.getFoodsList(StorageManager.FoodsList.BREAKFAST);
        totArray.addAll(storageManager.getFoodsList(StorageManager.FoodsList.LUNCH));
        totArray.addAll(storageManager.getFoodsList(StorageManager.FoodsList.DINNER));

        for(Food f: totArray) {
            totCalories += f.getCalorie();
        }

        this.totCalories.setText("" + totCalories);
    }

    private View.OnClickListener handleBreakfastClick = view -> {
        Bundle bundle = new Bundle();
        bundle.putString("typology", StorageManager.FoodsList.BREAKFAST.toString());
        NavHostFragment.findNavController(CaloriesFragment.this).navigate(
                R.id.action_nav_calories_to_nav_calories_list,
                bundle);
    };

    private View.OnClickListener handleLunchClick = view -> {
        Bundle bundle = new Bundle();
        bundle.putString("typology", StorageManager.FoodsList.LUNCH.toString());
        NavHostFragment.findNavController(CaloriesFragment.this).navigate(
                R.id.action_nav_calories_to_nav_calories_list,
                bundle);
    };

    private View.OnClickListener handleDinnerClick = view -> {
        Bundle bundle = new Bundle();
        bundle.putString("typology", StorageManager.FoodsList.DINNER.toString());
        NavHostFragment.findNavController(CaloriesFragment.this).navigate(
                R.id.action_nav_calories_to_nav_calories_list,
                bundle);
    };
}

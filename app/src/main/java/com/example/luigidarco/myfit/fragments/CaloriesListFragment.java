package com.example.luigidarco.myfit.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.utilities.FoodAdapter;
import com.example.luigidarco.myfit.utilities.StorageManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class CaloriesListFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;
    private ArrayList<Food> foodsList;

    private FloatingActionButton newElement;
    private ListView listView;
    private FoodAdapter foodAdapter;

    private StorageManager.FoodsList currentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity().getApplicationContext());

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        String title = "";

        String argument = getArguments().getString("typology");
        if (argument.equals(StorageManager.FoodsList.BREAKFAST.toString())) {
            currentList = StorageManager.FoodsList.BREAKFAST;
            title = "Breakfast";
        } else if (argument.equals(StorageManager.FoodsList.LUNCH.toString())) {
            currentList = StorageManager.FoodsList.LUNCH;
            title = "Lunch";
        } else if (argument.equals(StorageManager.FoodsList.DINNER.toString())) {
            currentList = StorageManager.FoodsList.DINNER;
            title = "Dinner";
        }

        toolbar.setTitle(title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calories_list, container, false);

        newElement = view.findViewById(R.id.new_element);
        newElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaloriesDialog dialog = new CaloriesDialog(currentList);
                dialog.display(getChildFragmentManager());
            }
        });

        listView = view.findViewById(R.id.calories_list_view);

        //change dynamically with other categories
        foodsList = storageManager.getFoodsList(currentList);

        foodAdapter = new FoodAdapter(foodsList, getActivity().getApplicationContext());
        listView.setAdapter(foodAdapter);

        return view;
    }

    public void updateList() {
        foodsList = storageManager.getFoodsList(currentList);
        foodAdapter.notifyDataSetChanged();
        Log.d(TAG, "Update");
        Log.d(TAG, foodsList.toString());
    }
}

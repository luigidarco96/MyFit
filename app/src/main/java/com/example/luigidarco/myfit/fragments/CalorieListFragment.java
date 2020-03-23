package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.utilities.FoodAdapter;
import com.example.luigidarco.myfit.utilities.StorageManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CalorieListFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;
    private ArrayList<Food> foodsList;

    private FloatingActionButton newElement;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;

    private StorageManager.FoodsList currentList;

    public CalorieListFragment(StorageManager.FoodsList typology) {
        this.currentList = typology;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calorie_list, container, false);

        newElement = view.findViewById(R.id.new_element);
        newElement.setOnClickListener(clickView -> {
            CalorieDialog dialog = new CalorieDialog(currentList);
            dialog.setCloseCallback(newObject -> {
                if (newObject != null) {
                    foodsList.add((Food) newObject);
                    foodAdapter.notifyDataSetChanged();
                }
            });
            dialog.display(getChildFragmentManager());
        });

        recyclerView = view.findViewById(R.id.calories_recycler_view);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        foodsList = storageManager.getFoodsList(currentList);

        for(Food f:foodsList) {
            Log.d(TAG, f.toString());
        }

        foodAdapter = new FoodAdapter(foodsList, getActivity().getApplicationContext());
        recyclerView.setAdapter(foodAdapter);

        return view;
    }
}

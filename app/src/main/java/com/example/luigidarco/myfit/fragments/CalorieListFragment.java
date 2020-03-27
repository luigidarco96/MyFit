package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.adapters.FoodAdapter;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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
            Bundle bundle = new Bundle();
            bundle.putString("currentList", currentList.toString());
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_calorie_to_add_food, bundle);
        });

        recyclerView = view.findViewById(R.id.calories_recycler_view);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        foodsList = storageManager.getFoodsList(currentList);

        foodAdapter = new FoodAdapter(foodsList, getActivity(), deleteItem -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Do you want to delete this item?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        storageManager.deleteElementFromFoodsList(currentList, deleteItem);
                        foodsList.remove(deleteItem);
                        foodAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {

                    })
                    .show();
        });
        recyclerView.setAdapter(foodAdapter);

        return view;
    }
}

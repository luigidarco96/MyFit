package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Workout;
import com.example.luigidarco.myfit.utilities.StorageManager;
import com.example.luigidarco.myfit.utilities.WorkoutAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class WorkoutListFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;
    private ArrayList<Workout> workoutList;

    private FloatingActionButton newElement;
    private RecyclerView recyclerView;
    private WorkoutAdapter workoutAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        newElement = view.findViewById(R.id.new_element);
        newElement.setOnClickListener(clickView -> {
            WorkoutDialog dialog = new WorkoutDialog();
            dialog.setCloseCallback(newObject -> {
                if (newObject != null) {
                    workoutList.add((Workout) newObject);
                    workoutAdapter.notifyDataSetChanged();
                }
            });
            dialog.display(getChildFragmentManager());
        });

        recyclerView = view.findViewById(R.id.workout_recycler_view);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        workoutList = storageManager.getWorkoutList();

        workoutAdapter = new WorkoutAdapter(workoutList, getActivity().getApplicationContext());
        recyclerView.setAdapter(workoutAdapter);

        return view;
    }
}

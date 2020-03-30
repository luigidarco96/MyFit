package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.models.Workout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WorkoutOverviewFragment extends Fragment {

    StorageManager storageManager;
    ArrayList<Workout> workouts;

    TextView totalTime;
    TextView numberExercises;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workout_overview, container, false);

        storageManager = new StorageManager(getContext());


        totalTime = view.findViewById(R.id.workout_overview_time);
        numberExercises = view.findViewById(R.id.workout_overview_exercises);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        workouts = storageManager.getWorkoutList();

        totalTime.setText(getTotalTime(workouts) + " minutes");
        numberExercises.setText(getNumberExercises(workouts) + "");
    }

    private int getTotalTime(ArrayList<Workout> workouts) {
        int tot = 0;
        for (Workout w: workouts) {
            tot += w.getTime();
        }
        return tot;
    }

    private int getNumberExercises(ArrayList<Workout> workouts) {
        return workouts.size();
    }
}

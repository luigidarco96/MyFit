package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;

import com.example.luigidarco.myfit.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String property = getResources().getString(R.string.app_preferences);

        if (property.equals("MIBAND")) {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_main_to_nav_home);
        } else {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_main_to_nav_robot);
        }
    }
}

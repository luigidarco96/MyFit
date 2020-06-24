package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.StorageManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class MainFragment extends Fragment {

    private StorageManager spManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spManager = new StorageManager(getContext());

        String property = spManager.getAppPreference();

        if (property.equals("FIT")) {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_main_to_nav_home);
        } else {
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_main_to_nav_robot);
        }
    }
}

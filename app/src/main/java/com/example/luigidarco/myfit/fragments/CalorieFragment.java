package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class CalorieFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calorie, container, false);

        tabLayout = view.findViewById(R.id.calories_tab_layout);
        viewPager = view.findViewById(R.id.calories_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(new CalorieOverviewFragment(), "Overview");
        viewPagerAdapter.addFragment(new CalorieListFragment(StorageManager.FoodsList.BREAKFAST), "Breakfast");
        viewPagerAdapter.addFragment(new CalorieListFragment(StorageManager.FoodsList.LUNCH), "Lunch");
        viewPagerAdapter.addFragment(new CalorieListFragment(StorageManager.FoodsList.DINNER), "Dinner");
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }
}

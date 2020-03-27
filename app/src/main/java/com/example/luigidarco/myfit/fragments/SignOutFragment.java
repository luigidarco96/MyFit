package com.example.luigidarco.myfit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.SignInActivity;
import com.example.luigidarco.myfit.managers.StorageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignOutFragment extends Fragment {

    private StorageManager storageManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        storageManager = new StorageManager(getContext());
        storageManager.deleteAccessToken();
        storageManager.deleteRefreshToken();

        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);

        return null;
    }
}

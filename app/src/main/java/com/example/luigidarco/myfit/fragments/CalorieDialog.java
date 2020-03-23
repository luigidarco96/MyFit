package com.example.luigidarco.myfit.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.utilities.CloseDialogCallback;
import com.example.luigidarco.myfit.utilities.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class CalorieDialog extends DialogFragment {

    public static final String TAG = "MYFITAPP";

    private StorageManager storageManager;
    private StorageManager.FoodsList currentList;

    private Toolbar toolbar;
    private ImageView image;
    private TextInputLayout name;
    private TextInputLayout calorie;

    private CloseDialogCallback closeDialogCallback;

    public void setCloseCallback(CloseDialogCallback callback) {
        this.closeDialogCallback = callback;
    }

    public CalorieDialog(StorageManager.FoodsList currentList) {
        this.currentList = currentList;
    }

    public void display(FragmentManager fragmentManager) {
        this.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        storageManager = new StorageManager(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_calories, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        image = view.findViewById(R.id.food_image);
        name = view.findViewById(R.id.food_name);
        calorie = view.findViewById(R.id.food_calories);

        return view;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.setTitle("Add dish");
        toolbar.inflateMenu(R.menu.dialog_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Food food = new Food(
                        null, //replace with image
                        name.getEditText().getText().toString(),
                        Integer.parseInt(calorie.getEditText().getText().toString())
                );
                storageManager.addElementToFoodsList(currentList, food);
                closeDialogCallback.onAction(food);
                dismiss();
                return false;
            }
        });
    }
}

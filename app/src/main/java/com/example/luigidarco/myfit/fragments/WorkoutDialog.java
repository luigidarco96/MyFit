package com.example.luigidarco.myfit.fragments;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Workout;
import com.example.luigidarco.myfit.utilities.CloseDialogCallback;
import com.example.luigidarco.myfit.utilities.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class WorkoutDialog extends DialogFragment {

    public static final String TAG = "MYFITAPP";

    private StorageManager storageManager;

    private Toolbar toolbar;
    private Spinner spinner;
    private TextInputLayout time;

    private CloseDialogCallback closeDialogCallback;

    public void setCloseCallback(CloseDialogCallback callback) {
        this.closeDialogCallback = callback;
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

        View view = inflater.inflate(R.layout.dialog_workout, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        spinner = view.findViewById(R.id.workout_spinner);
        time = view.findViewById(R.id.workout_time);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(cview -> dismiss());
        toolbar.setTitle("Add activity");
        toolbar.inflateMenu(R.menu.dialog_menu);
        toolbar.setOnMenuItemClickListener(menu -> {
            int position = spinner.getSelectedItemPosition();
            String itemSelected = spinner.getSelectedItem().toString();
            TypedArray imgs = getResources().obtainTypedArray(R.array.exercise_icon_array);
            int iconSelected = imgs.getResourceId(position + 1, -1);
            Workout workout = new Workout(
                    iconSelected,
                    itemSelected,
                    Integer.parseInt(time.getEditText().getText().toString())
            );
            storageManager.addElementToWorkoutList(workout);
            closeDialogCallback.onAction(workout);
            dismiss();
            return false;
        });
    }
}

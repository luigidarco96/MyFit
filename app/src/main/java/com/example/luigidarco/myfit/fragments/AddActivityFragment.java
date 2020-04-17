package com.example.luigidarco.myfit.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Workout;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AddActivityFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;

    private LinearLayout titleLayout;
    private LinearLayout dateLayout;
    private LinearLayout durationLayout;
    private TextView typeTextView;
    private TextView dateTextView;
    private TextView durationTextView;
    private Button saveButton;

    private int tmpType = 0;

    private Workout workout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity().getApplicationContext());
        workout = new Workout();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_activity, container, false);

        titleLayout = view.findViewById(R.id.dialog_element_title);
        dateLayout = view.findViewById(R.id.dialog_element_date);
        durationLayout = view.findViewById(R.id.dialog_element_duration);
        typeTextView = view.findViewById(R.id.add_activity_type);
        dateTextView = view.findViewById(R.id.add_activity_date);
        durationTextView = view.findViewById(R.id.add_activity_duration);
        saveButton = view.findViewById(R.id.add_activity_save);

        titleLayout.setOnClickListener(handleTitle);
        dateLayout.setOnClickListener(handleDate);
        durationLayout.setOnClickListener(handleDuration);
        saveButton.setOnClickListener(handleSave);

        return view;
    }

    private View.OnClickListener handleTitle = view -> {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select new activity")
                .setSingleChoiceItems(R.array.exercise_title_array, 0, ((dialogInterface, i) -> {
                    tmpType = i;
                }))
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    String itemSelected = getResources().getStringArray(R.array.exercise_title_array)[tmpType];
                    TypedArray imgs = getResources().obtainTypedArray(R.array.exercise_icon_array);
                    int iconSelected = imgs.getResourceId(tmpType, -1);
                    workout.setImageResource(iconSelected);
                    workout.setTitle(itemSelected);
                    typeTextView.setText(itemSelected);
                }))
                .setNegativeButton("Cancel", null)
                .show();
    };

    private View.OnClickListener handleDate = view -> {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int cMonth = month + 1;
                String date = year + "-" + (cMonth < 10 ? "0" + cMonth : cMonth) + "-" + (day < 10 ? "0" + day : day);
                workout.setDate(date);
                dateTextView.setText(day + "/" + cMonth + "/" + year);
            }
        }, year, month, day)
                .show();
    };

    private View.OnClickListener handleDuration = view -> {
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                durationTextView.setText(hour + ":" + minutes);
                workout.setTime(hour*60+minutes);
            }
        }, 0, 0, true)
                .show();
    };

    private View.OnClickListener handleSave = view -> {
        //check if all is set
        storageManager.addElementToWorkoutList(workout);
        NavHostFragment.findNavController(getParentFragment()).navigateUp();
    };
}

package com.example.luigidarco.myfit.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AddFoodFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private final int GALLERY_REQUEST_CODE = 101;

    private StorageManager storageManager;
    private StorageManager.FoodsList currentList;

    private LinearLayout imageLayout;
    private LinearLayout titleLayout;
    private LinearLayout dateLayout;
    private LinearLayout calorieLayout;
    private ImageView imageView;
    private TextView typeTextView;
    private TextView dateTextView;
    private TextView calorieTextView;
    private Button saveButton;

    private Food food;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new StorageManager(getActivity().getApplicationContext());

        String currentList = getArguments().getString("currentList", "");

        if (currentList.equals(StorageManager.FoodsList.BREAKFAST.toString())) {
            this.currentList = StorageManager.FoodsList.BREAKFAST;
        } else if (currentList.equals(StorageManager.FoodsList.LUNCH.toString())) {
            this.currentList = StorageManager.FoodsList.LUNCH;
        } else if (currentList.equals(StorageManager.FoodsList.DINNER.toString())) {
            this.currentList = StorageManager.FoodsList.DINNER;
        }

        food = new Food();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_food, container, false);

        imageLayout = view.findViewById(R.id.dialog_element_camera);
        titleLayout = view.findViewById(R.id.dialog_element_title);
        dateLayout = view.findViewById(R.id.dialog_element_date);
        calorieLayout = view.findViewById(R.id.dialog_element_calorie);
        imageView = view.findViewById(R.id.add_food_image);
        typeTextView = view.findViewById(R.id.add_food_type);
        dateTextView = view.findViewById(R.id.add_food_date);
        calorieTextView = view.findViewById(R.id.add_food_calorie);
        saveButton = view.findViewById(R.id.add_activity_save);

        imageLayout.setOnClickListener(handleCamera);
        titleLayout.setOnClickListener(handleTitle);
        dateLayout.setOnClickListener(handleDate);
        calorieLayout.setOnClickListener(handleCalorie);
        saveButton.setOnClickListener(handleSave);

        return view;
    }

    private View.OnClickListener handleCamera = view -> {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
    };

    private View.OnClickListener handleTitle = view -> {
        EditText editText = new EditText(getContext());
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Insert new dish")
                .setView(editText)
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    typeTextView.setText(editText.getText().toString());
                    food.setName(editText.getText().toString());
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
                food.setDate(date);
                dateTextView.setText(day + "/" + cMonth + "/" + year);
            }
        }, year, month, day)
                .show();
    };

    private View.OnClickListener handleCalorie = view -> {
        EditText editText = new EditText(getContext());
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Insert calorie")
                .setView(editText)
                .setPositiveButton("Ok", ((dialogInterface, i) -> {
                    calorieTextView.setText(editText.getText().toString());
                    food.setCalorie(Integer.parseInt(editText.getText().toString()));
                }))
                .setNegativeButton("Cancel", null)
                .show();
    };

    private View.OnClickListener handleSave = view -> {
        //check if all is set
        storageManager.addElementToFoodsList(currentList, food);
        NavHostFragment.findNavController(getParentFragment()).navigateUp();
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream;
                        imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);

                        // Send image
                        String url = getResources().getString(R.string.url_server) + "/images";
                        NetworkManager.sendImage(getContext(), url, selectedImage, new NetworkCallback() {
                            @Override
                            public void onSuccess(JSONObject object) {
                                try {
                                    String imageUrl = object.getString("data");
                                    Log.d(TAG, imageUrl);
                                    food.setImagePath(imageUrl);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

            }
    }
}

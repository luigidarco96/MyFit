package com.example.luigidarco.myfit.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.models.PersonalInfo;
import com.example.luigidarco.myfit.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private String TAG = "MYFITAPP";

    private User currentUser;
    private PersonalInfo personalInfo;

    private ImageView image;
    private TextView username;
    private TextView fullName;
    private TextView weight;
    private TextView height;
    private TextView gender;
    private TextView dateOfBirth;
    private ImageView fullNameEdit;
    private ImageView weightEdit;
    private ImageView heightEdit;
    private ImageView genderEdit;
    private ImageView dateOfBirthEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.profile_username);
        fullName = view.findViewById(R.id.profile_name);
        weight = view.findViewById(R.id.profile_weight);
        height = view.findViewById(R.id.profile_height);
        gender = view.findViewById(R.id.profile_gender);
        dateOfBirth = view.findViewById(R.id.profile_birth);
        fullNameEdit = view.findViewById(R.id.profile_edit_name);
        weightEdit = view.findViewById(R.id.profile_edit_weight);
        heightEdit = view.findViewById(R.id.profile_edit_height);
        genderEdit = view.findViewById(R.id.profile_edit_gender);
        dateOfBirthEdit = view.findViewById(R.id.profile_edit_birth);

        username.setText(new StorageManager(getContext()).getCurrentUser().getUsername());

        fullNameEdit.setOnClickListener(fullNameEditListener);
        weightEdit.setOnClickListener(weightEditListener);
        heightEdit.setOnClickListener(heightEditListener);
        genderEdit.setOnClickListener(genderEditListener);
        dateOfBirthEdit.setOnClickListener(birthEditListener);

        initInformation();
        return view;
    }

    private void initInformation() {
        String url = getResources().getString(R.string.url_server) + "/users";

        NetworkManager.makeGetJsonObjRequest(getContext(), url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject data = object.getJSONObject("data");
                    currentUser = new User(
                            data.getInt("id"),
                            data.getString("username"),
                            data.getString("full_name"),
                            data.getString("date_of_birth"),
                            data.getInt("gender")
                    );

                    String url = getResources().getString(R.string.url_server) + "/personal-info";
                    NetworkManager.makeGetJsonObjRequest(getContext(), url, new NetworkCallback() {
                        @Override
                        public void onSuccess(JSONObject object) {
                            try {
                                JSONArray data = object.getJSONArray("data");
                                JSONObject obj = data.getJSONObject(0);
                                personalInfo = new PersonalInfo(
                                        obj.getInt("id"),
                                        obj.getDouble("weight"),
                                        obj.getDouble("height"),
                                        obj.getDouble("bmi"),
                                        obj.getString("bmi_class"),
                                        new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss").parse(obj.getString("timestamp"))
                                );

                                showInformation();

                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "Error" + error);
                new MaterialAlertDialogBuilder(getContext())
                        .setMessage("Something went wrong during the loading, try again!")
                        .show();
            }
        });
    }

    private void showInformation() {
        Log.d(TAG, currentUser.toString());
        fullName.setText(currentUser.getFullName());
        weight.setText(personalInfo.getWeight() + " Kg");
        height.setText(personalInfo.getHeight() + " m");
        if (currentUser.getGender() == 0) {
            gender.setText("Male");
            image.setImageResource(R.drawable.ic_male);
        } else {
            gender.setText("Female");
            image.setImageResource(R.drawable.ic_female);
        }
        dateOfBirth.setText(currentUser.getDateBirth());
    }

    private View.OnClickListener fullNameEditListener = view -> {
        LayoutInflater inflater = getLayoutInflater();
        View editView = inflater.inflate(R.layout.dialog_edit_text, null);
        EditText editText = editView.findViewById(R.id.dialog_edit_text);
        editText.setText(currentUser.getFullName());
        new MaterialAlertDialogBuilder(getContext())
                .setMessage("Insert your full name")
                .setView(editView)
                .setCancelable(false)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newFullName = editText.getText().toString();
                    currentUser.setFullName(newFullName);
                    fullName.setText(newFullName);
                    updateUser();
                })
                .setNegativeButton("Cancel", null)
                .show();
    };

    private View.OnClickListener genderEditListener = view -> {
        Toast.makeText(getContext(), "Ciao", Toast.LENGTH_SHORT).show();
    };

    private View.OnClickListener birthEditListener = view -> {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
            int cMonth = month1 + 1;
            String date =  (day1 < 10 ? "0" + day1 : day1) + "/" + (cMonth < 10 ? "0" + cMonth : cMonth) + "/" + year1;
            currentUser.setDateBirth(date);
            dateOfBirth.setText(date);
            updateUser();
        }, year, month, day)
                .show();
    };

    private View.OnClickListener weightEditListener = view -> {
        LayoutInflater inflater = getLayoutInflater();
        View editView = inflater.inflate(R.layout.dialog_edit_text, null);
        EditText editText = editView.findViewById(R.id.dialog_edit_text);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("" + personalInfo.getWeight());
        new MaterialAlertDialogBuilder(getContext())
                .setMessage("Insert your weight")
                .setView(editView)
                .setCancelable(false)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newWeight = editText.getText().toString();
                    personalInfo.setWeight(Double.parseDouble(newWeight));
                    weight.setText(newWeight + " Kg");
                    updatePersonalInformation();
                })
                .setNegativeButton("Cancel", null)
                .show();
    };

    private View.OnClickListener heightEditListener = view -> {
        LayoutInflater inflater = getLayoutInflater();
        View editView = inflater.inflate(R.layout.dialog_edit_text, null);
        EditText editText = editView.findViewById(R.id.dialog_edit_text);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("" + personalInfo.getHeight());
        new MaterialAlertDialogBuilder(getContext())
                .setMessage("Insert your height")
                .setView(editView)
                .setCancelable(false)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String newHeight = editText.getText().toString();
                    personalInfo.setHeight(Double.parseDouble(newHeight));
                    height.setText(newHeight + " m");
                    updatePersonalInformation();
                })
                .setNegativeButton("Cancel", null)
                .show();
    };

    private void updateUser() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("full_name", currentUser.getFullName());
            obj.put("date_of_birth", currentUser.getDateBirthSQL());
            obj.put("gender", currentUser.getGender());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.url_server) + "/users";

        NetworkManager.makePutJsonObjRequest(getContext(), url, obj, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                new MaterialAlertDialogBuilder(getContext())
                        .setMessage("User information updated")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onError(String error) {
                new MaterialAlertDialogBuilder(getContext())
                        .setMessage("Something goes wrong, please try again")
                        .show();
            }
        });
    }

    private void updatePersonalInformation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("weight", personalInfo.getWeight());
            obj.put("height", personalInfo.getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.url_server) + "/personal-info";

        NetworkManager.makePostJsonObjRequest(getContext(), url, obj, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                new MaterialAlertDialogBuilder(getContext())
                        .setMessage("Personal information updated")
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onError(String error) {
                new MaterialAlertDialogBuilder(getContext())
                        .setMessage("Something goes wrong, please try again")
                        .show();
            }
        });
    }
}

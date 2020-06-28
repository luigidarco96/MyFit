package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private String TAG = "MYFITAPP";

    private ImageView image;
    private TextView username;
    private TextView fullName;
    private TextView weight;
    private TextView height;
    private TextView gender;
    private TextView dateOfBirth;

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
                    fullName.setText(data.getString("full_name"));
                    weight.setText(data.getInt("weight") + " Kg");
                    height.setText(data.getInt("height") + " cm");
                    int tmp_gender = data.getInt("gender");
                    if (tmp_gender == 0) {
                        gender.setText("Male");
                        image.setImageResource(R.drawable.ic_male);
                    } else {
                        gender.setText("Female");
                        image.setImageResource(R.drawable.ic_female);
                    }
                    dateOfBirth.setText(data.getString("date_of_birth"));

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
}

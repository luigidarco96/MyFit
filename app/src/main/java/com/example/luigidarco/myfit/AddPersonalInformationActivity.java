package com.example.luigidarco.myfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddPersonalInformationActivity extends AppCompatActivity {

    private String TAG = "MYFITAPP";

    StorageManager spManager;
    String appPreference;

    TextInputLayout fullName;
    TextInputLayout weight;
    TextInputLayout height;
    TextInputLayout dateOfBirth;
    Button continueButton;
    private SegmentedButtonGroup gender;
    LinearLayout errorLayout;
    TextView errorText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_information);

        spManager = new StorageManager(this);
        appPreference = spManager.getAppPreference();

        fullName = findViewById(R.id.user_name);
        weight = findViewById(R.id.user_weight);
        height = findViewById(R.id.user_height);
        dateOfBirth = findViewById(R.id.user_date_of_birth);
        continueButton = findViewById(R.id.user_button);
        gender = findViewById(R.id.user_gender);
        errorLayout = findViewById(R.id.user_error);
        errorText = findViewById(R.id.user_error_text);

        continueButton.setOnClickListener(onClickContinue);
    }

    private View.OnClickListener onClickContinue = view -> {
        hideError();

        String fullName = this.fullName.getEditText().getText().toString();
        int weight = Integer.parseInt(this.weight.getEditText().getText().toString());
        int height = Integer.parseInt(this.height.getEditText().getText().toString());
        String date = this.dateOfBirth.getEditText().getText().toString();
        int gender = this.gender.getPosition();

        if (fullName.equals("")) {
            showError("Missing fields");
        } else {
            JSONObject params = new JSONObject();
            try {
                params.put("full_name", fullName);
                params.put("weight", weight);
                params.put("height", height);
                params.put("date_of_birth", date);
                params.put("gender", gender);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, params.toString());
            String url = getResources().getString(R.string.url_server) + "/users";
            NetworkManager.makePutJsonObjRequest(this, url, params, new NetworkCallback() {
                @Override
                public void onSuccess(JSONObject object) {
                    startSession();
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error: " + error);
                    showError("Something went wrong!");
                }
            });
        }
    };

    private void showError(String msg) {
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }

    private void hideError() {
        errorLayout.setVisibility(View.GONE);
    }

    private void startSession() {
        Intent intent;
        if (appPreference.equals("FIT")) {
            intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

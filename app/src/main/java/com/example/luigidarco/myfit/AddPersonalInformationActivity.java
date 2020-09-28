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

    private StorageManager spManager;

    private TextInputLayout weight;
    private TextInputLayout height;
    private Button continueButton;
    private LinearLayout errorLayout;
    private TextView errorText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_information);

        spManager = new StorageManager(this);

        weight = findViewById(R.id.user_weight);
        height = findViewById(R.id.user_height);
        continueButton = findViewById(R.id.user_button);
        errorLayout = findViewById(R.id.user_error);
        errorText = findViewById(R.id.user_error_text);

        continueButton.setOnClickListener(onClickContinue);
    }

    private boolean checkEmpty(double weight, double height) {
        if (weight < 1) {
            showError("Weight required greater than 1");
            return false;
        }
        if (height < 1) {
            showError("Height required greater than 1");
            return false;
        }
        return true;
    }

    private View.OnClickListener onClickContinue = view -> {
        hideError();

        double weight = Double.parseDouble(this.weight.getEditText().getText().toString());
        double height = Double.parseDouble(this.height.getEditText().getText().toString());

        if (!checkEmpty(weight, height)) {
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put("weight", weight);
            params.put("height", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, params.toString());
        String url = getResources().getString(R.string.url_server) + "/personal-info";
        NetworkManager.makePostJsonObjRequest(this, url, params, new NetworkCallback() {
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
        intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

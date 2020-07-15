package com.example.luigidarco.myfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SignUpActivity extends Activity {

    String TAG = "MYFITAPP";

    TextInputLayout username;
    TextInputLayout password;
    TextInputLayout confirmPassword;
    TextInputLayout fullName;
    TextInputLayout dateBirth;
    SegmentedButtonGroup gender;
    Button signUpButton;

    LinearLayout errorLayout;
    TextView errorText;

    StorageManager spManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        username = findViewById(R.id.sign_up_username);
        password = findViewById(R.id.sign_up_password);
        confirmPassword = findViewById(R.id.sign_up_confirm_password);
        fullName = findViewById(R.id.sign_up_full_name);
        dateBirth = findViewById(R.id.sign_up_date_of_birth);
        gender = findViewById(R.id.sing_up_gender);

        signUpButton = findViewById(R.id.sign_up_button);

        errorLayout = findViewById(R.id.sign_up_error_layout);
        errorText = findViewById(R.id.sign_up_error_text);

        signUpButton.setOnClickListener(signUpButtonListener);

        spManager = new StorageManager(this);
    }

    private boolean checkEmptyField(String username, String password, String confirmPassword, String fullName, String dateBirth) {
        if (username.equals("")) {
            showError("Username required");
            return false;
        }
        if (password.equals("")) {
            showError("Password required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showError("Password and Confirm password must be equal");
            return false;
        }
        if (fullName.equals("")) {
            showError("Full name required");
            return false;
        }
        if (dateBirth.equals("")) {
            showError("Date of birth required");
            return false;
        }
        return true;
    }

    private void hideError() {
        errorLayout.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }

    private View.OnClickListener signUpButtonListener = (view) -> {
        hideError();

        final String username = this.username.getEditText().getText().toString();
        String password = this.password.getEditText().getText().toString();
        String confirmPassword = this.confirmPassword.getEditText().getText().toString();
        String fullName = this.fullName.getEditText().getText().toString();
        String dateOfBirth = this.dateBirth.getEditText().getText().toString();
        int gender = this.gender.getPosition();

        if (!checkEmptyField(username, password, confirmPassword, fullName, dateOfBirth)) {
            return;
        }

        String url = getResources().getString(R.string.url_server) + "/users";

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);
            params.put("confirm_password", confirmPassword);
            params.put("full_name", fullName);
            params.put("date_of_birth", dateOfBirth);
            params.put("gender", gender);

            NetworkManager.makePostJsonObjRequest(this, url, params, new NetworkCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject data = response.getJSONObject("data");

                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    errorLayout.setVisibility(View.VISIBLE);
                    errorText.setText(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };
}

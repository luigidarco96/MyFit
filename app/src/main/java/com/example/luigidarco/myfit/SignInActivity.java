package com.example.luigidarco.myfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.managers.PermissionManager;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SignInActivity extends Activity implements View.OnClickListener {

    final String TAG = "MYFITAPP";

    private String appPreference;

    TextInputLayout username;
    TextInputLayout password;
    Button loginButton;
    TextView loginCreateAccount;

    LinearLayout errorLayout;
    TextView errorText;

    StorageManager spManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        appPreference = getResources().getString(R.string.app_preferences);

        spManager = new StorageManager(this);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        errorLayout = findViewById(R.id.sign_in_error_layout);
        errorText = findViewById(R.id.sign_in_error_text);

        loginButton.setOnClickListener(this);
        loginCreateAccount = findViewById(R.id.login_create_account);
        loginCreateAccount.setOnClickListener(cview -> {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {
        errorLayout.setVisibility(View.GONE);

        if (
                username.getEditText().getText().toString().equals("")
                || password.getEditText().getText().toString().equals("")
        ) {
            return;
        }

        String url = getResources().getString(R.string.url_server) + "/sign-in";

        JSONObject params = new JSONObject();
        try {
            params.put("username", username.getEditText().getText().toString());
            params.put("password", password.getEditText().getText().toString());

            NetworkManager.makePostJsonObjRequest(this, url, params, new NetworkCallback() {
                @Override
                public void onSuccess(JSONObject object) {
                    try {
                        JSONObject data = object.getJSONObject("data");
                        spManager.setUsername(username.getEditText().getText().toString());
                        spManager.setAccessToken(data.getString("access_token"));
                        spManager.setRefreshToken(data.getString("refresh_token"));

                        if (appPreference.equals("MIBAND")) {
                            Intent intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionManager.checkLocationUsage(this) && PermissionManager.isBluetoothEnabled(this)) {
                checkUserAlreadyLoggedIn();
        }
    }

    private void checkUserAlreadyLoggedIn() {
        if (spManager.getAccessToken() != "") {

            if (appPreference.equals("MIBAND")) {
                Intent intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }
    }
}

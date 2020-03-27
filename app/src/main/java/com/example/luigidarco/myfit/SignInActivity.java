package com.example.luigidarco.myfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SignInActivity extends Activity implements View.OnClickListener {

    final String TAG = "MYFITAPP";

    TextInputLayout username;
    TextInputLayout password;
    Button loginButton;
    TextView loginCreateAccount;

    StorageManager spManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        spManager = new StorageManager(this);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        loginCreateAccount = findViewById(R.id.login_create_account);
        loginCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        checkUserAlreadyLoggedIn();
    }

    @Override
    public void onClick(View view) {
        String url = getResources().getString(R.string.url_server) + "/login";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("username", username.getEditText().getText().toString());
        params.put("password", password.getEditText().getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(
                url,
                new JSONObject(params),
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        spManager.setUsername(username.getEditText().getText().toString());
                        spManager.setAccessToken(data.getString("access_token"));
                        spManager.setRefreshToken(data.getString("refresh_token"));

                        Intent intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, response.toString());
                },
                error -> {
                    Log.d(TAG, error.toString());
                });

        queue.add(request);
    }

    private void checkUserAlreadyLoggedIn() {
        if (spManager.getAccessToken() != "") {
            Intent intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
            startActivity(intent);
        }
    }
}

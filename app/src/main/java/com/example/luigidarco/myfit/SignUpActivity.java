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
import com.example.luigidarco.myfit.managers.StorageManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SignUpActivity extends Activity implements View.OnClickListener {

    String TAG = "MYFITAPP";

    TextInputLayout username;
    TextInputLayout password;
    TextInputLayout confirmPassword;
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
        signUpButton = findViewById(R.id.sign_up_button);

        errorLayout = findViewById(R.id.sign_up_error_layout);
        errorText = findViewById(R.id.sign_up_error_text);

        signUpButton.setOnClickListener(this);

        spManager = new StorageManager(this);
    }

    @Override
    public void onClick(View view) {
        errorLayout.setVisibility(View.GONE);

        final String username = this.username.getEditText().getText().toString();
        String password = this.password.getEditText().getText().toString();
        String confirmPassword = this.confirmPassword.getEditText().getText().toString();

        if (!password.equals(confirmPassword)) {
            //error
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText("Password and Confirm Password are not equals");
        } else {
            String url = getResources().getString(R.string.url_server) + "/users";
            RequestQueue queue = Volley.newRequestQueue(this);

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                    response -> {

                        try {
                            spManager.setUsername(username);
                            spManager.setAccessToken(response.getString("access_token"));
                            spManager.setRefreshToken(response.getString("refresh_token"));

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, response.toString());
                    },
                    error -> {
                        errorLayout.setVisibility(View.VISIBLE);
                        errorText.setText(error.toString());

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorText.setText("Server unreachable");
                        } else if (error instanceof AuthFailureError) {
                            errorText.setText("Username already exists");
                        } else if (error instanceof ServerError) {
                            errorText.setText("Something goes wrong. Try again");
                        } else if (error instanceof NetworkError) {
                            errorText.setText("Check your connection");
                        } else if (error instanceof ParseError) {
                            errorText.setText("Something goes wrong. Try again");
                        }

                        Log.d(TAG, error.toString());
                    });

            queue.add(request);

        }
    }
}

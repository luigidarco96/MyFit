package com.example.luigidarco.myfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

    StorageManager spManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        username = findViewById(R.id.sign_up_username);
        password = findViewById(R.id.sign_up_password);
        confirmPassword = findViewById(R.id.sign_up_confirm_password);
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);

        spManager = new StorageManager(this);
    }

    @Override
    public void onClick(View view) {
        final String username = this.username.getEditText().getText().toString();
        String password = this.password.getEditText().getText().toString();
        String confirmPassword =this.confirmPassword.getEditText().getText().toString();

        if (!password.equals(confirmPassword)) {
            //error
            Toast.makeText(this, "Password and Confirm Password are not equals", Toast.LENGTH_SHORT).show();
        } else {
            String url = getResources().getString(R.string.url_server) + "/users";
            RequestQueue queue = Volley.newRequestQueue(this);

            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.toString());
                        }
                    });

            queue.add(request);

        }
    }
}

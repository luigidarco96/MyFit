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
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
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

        if (username.equals("") || password.equals("")) {
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText("Username and Password can't be empty");
        }
        if (!password.equals(confirmPassword)) {
            //error
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText("Password and Confirm Password are not equals");
        } else {
            String url = getResources().getString(R.string.url_server) + "/users";

            JSONObject params = new JSONObject();
            try {
                params.put("username", username);
                params.put("password", password);

                NetworkManager.makePostJsonObjRequest(this, url, params, new NetworkCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            spManager.setUsername(username);
                            spManager.setAccessToken(data.getString("access_token"));
                            spManager.setRefreshToken(data.getString("refresh_token"));

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
        }
    }
}

package com.example.luigidarco.myfit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
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
import com.example.luigidarco.myfit.models.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class SignInActivity extends Activity {

    final String TAG = "MYFITAPP";

    private ProgressDialog loginDialog;
    private TextInputLayout username;
    private TextInputLayout password;
    private Button loginButton;
    private TextView loginCreateAccount;

    private LinearLayout errorLayout;
    private TextView errorText;

    private SegmentedButtonGroup segmentedButtonGroup;

    StorageManager spManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        spManager = new StorageManager(this);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        errorLayout = findViewById(R.id.sign_in_error_layout);
        errorText = findViewById(R.id.sign_in_error_text);
        segmentedButtonGroup = findViewById(R.id.sign_in_app_preference);

        loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Sign in...");
        loginDialog.setCancelable(false);

        loginButton.setOnClickListener(cView -> {
            String username = this.username.getEditText().getText().toString();
            String password = this.password.getEditText().getText().toString();

            if (username.equals("") || password.equals("")) {
                return;
            }
            int pref = segmentedButtonGroup.getPosition();

            login(username, password, pref);

        });

        loginCreateAccount = findViewById(R.id.login_create_account);
        loginCreateAccount.setOnClickListener(cView -> {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
        });

        loadStoredUser();
    }

    public void loadStoredUser() {
        User user = spManager.getCurrentUser();
        if (user != null) {
            refreshToken();
        }
    }

    private void login(String username, String password, int pref) {
        hideError();
        loginDialog.show();

        String url = getResources().getString(R.string.url_server) + "/sign-in";

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("password", password);

            NetworkManager.makePostJsonObjRequest(this, url, params, new NetworkCallback() {
                @Override
                public void onSuccess(JSONObject object) {
                    try {
                        JSONObject data = object.getJSONObject("data");
                        saveUser(data, pref);
                        checkNewUser();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    loginDialog.cancel();
                    showError(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshToken() {
        hideError();
        loginDialog.show();

        String url = getResources().getString(R.string.url_server) + "/refresh-token";
        int pref = spManager.getAppPreference().equals("FIT") ? 0 : 1;

        NetworkManager.refreshToken(this, url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                JSONObject data = null;
                try {
                    data = object.getJSONObject("data");
                    saveUser(data, pref);
                    checkNewUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                loginDialog.cancel();
                showError(error);
            }
        });
    }

    private void saveUser(JSONObject obj, int pref) throws JSONException {
        spManager.setUsername(username.getEditText().getText().toString());
        spManager.setAccessToken(obj.getString("access_token"));
        spManager.setRefreshToken(obj.getString("refresh_token"));
        spManager.setAppPreference(pref == 0 ? StorageManager.AppPreference.FIT : StorageManager.AppPreference.ROBOT);

        User currentUser = new User(
                obj.getInt("id"),
                obj.getString("username"),
                obj.getString("full_name"),
                obj.getString("date_of_birth"),
                obj.getInt("gender")
        );
        spManager.setCurrentUser(currentUser);
    }

    private void checkNewUser() {
        String url = getResources().getString(R.string.url_server) + "/personal-info";

        NetworkManager.makeGetJsonObjRequest(this, url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray data = object.getJSONArray("data");
                    if (data.length() == 0) {
                        //New User
                        Intent intent = new Intent(getApplicationContext(), AddPersonalInformationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        startSession();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error" + error);
            }
        });
    }

    private void startSession() {
        Intent intent;
        String appPreference = spManager.getAppPreference();
        if (appPreference.equals("FIT")) {
            intent = new Intent(getApplicationContext(), BluetoothDeviceListActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showError(String msg) {
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }

    private void hideError() {
        errorLayout.setVisibility(View.GONE);
    }
}

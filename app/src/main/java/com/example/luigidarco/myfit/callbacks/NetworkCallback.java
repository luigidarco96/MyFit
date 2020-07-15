package com.example.luigidarco.myfit.callbacks;

import org.json.JSONException;
import org.json.JSONObject;

public interface NetworkCallback {

    public void onSuccess(JSONObject object);
    public void onError(String error);
}

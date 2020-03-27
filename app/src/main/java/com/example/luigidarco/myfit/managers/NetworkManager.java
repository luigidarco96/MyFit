package com.example.luigidarco.myfit.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static final String TAG = "MYFITAPP";

    public static void makePostJsonObjRequest(Context mContext, String url, JSONObject jsonObject, NetworkCallback callback) {

        StorageManager storageManager = new StorageManager(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    callback.onSuccess(response);
                }, error -> {
            callback.onError(error.toString());
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + storageManager.getAccessToken());
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public static void sendImage(Context mContext, String url, Bitmap bitmap, NetworkCallback callback) {
        StorageManager storageManager = new StorageManager(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", imageString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //sending image to server
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    callback.onSuccess(response);
                },
                error -> {
                    callback.onError(error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + storageManager.getAccessToken());
                return headers;
            }
        };

        queue.add(request);
    }
}

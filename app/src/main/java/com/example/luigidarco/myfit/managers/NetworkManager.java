package com.example.luigidarco.myfit.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static final String TAG = "MYFITAPP";

    public static void makeGetJsonObjRequest(Context mContext, String url, NetworkCallback callback) {

        StorageManager storageManager = new StorageManager(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> callback.onSuccess(response),
                error -> {
                    String message = "";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callback.onError("Server unreachable");
                    } else if (error instanceof ServerError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof NetworkError) {
                        callback.onError("Check your connection");
                    } else if (error instanceof ParseError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof AuthFailureError) {
                        if (error.networkResponse.data != null) {
                            try {
                                String data = new String(error.networkResponse.data, "UTF-8");
                                Log.d(TAG, data);
                                JSONObject obj = new JSONObject(data);
                                message = obj.getString("message");
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onError(message);
                    }
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

    public static void makePostJsonObjRequest(Context mContext, String url, JSONObject jsonObject, NetworkCallback callback) {

        StorageManager storageManager = new StorageManager(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> callback.onSuccess(response),
                error -> {
                    String message = "";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callback.onError("Server unreachable");
                    } else if (error instanceof ServerError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof NetworkError) {
                        callback.onError("Check your connection");
                    } else if (error instanceof ParseError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof AuthFailureError) {
                        if (error.networkResponse.data != null) {
                            try {
                                String data = new String(error.networkResponse.data, "UTF-8");
                                Log.d(TAG, data);
                                JSONObject obj = new JSONObject(data);
                                message = obj.getString("message");
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onError(message);
                    }
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

    public static void makePutJsonObjRequest(Context mContext, String url, JSONObject jsonObject, NetworkCallback callback) {

        StorageManager storageManager = new StorageManager(mContext);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                response -> callback.onSuccess(response),
                error -> {
                    String message = "";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callback.onError("Server unreachable");
                    } else if (error instanceof ServerError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof NetworkError) {
                        callback.onError("Check your connection");
                    } else if (error instanceof ParseError) {
                        callback.onError("Something goes wrong. Try again");
                    } else if (error instanceof AuthFailureError) {
                        if (error.networkResponse.data != null) {
                            try {
                                String data = new String(error.networkResponse.data, "UTF-8");
                                Log.d(TAG, data);
                                JSONObject obj = new JSONObject(data);
                                message = obj.getString("message");
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onError(message);
                    }
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

    public static void makeDeleteRequest(Context context, String url, NetworkCallback callback) {
        StorageManager storageManager = new StorageManager(context);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                },
                error -> {
                    Log.e(TAG, error.toString());
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + storageManager.getAccessToken());
                return headers;
            }
        };

        queue.add(request);

    }

    public static void sendImage(Context mContext, String url, Bitmap bitmap, NetworkCallback
            callback) {
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

    public static void getImage(Context mContext, String url, ImageView imageView) {
        StorageManager storageManager = new StorageManager(mContext);

        GlideUrl glideUrl = new GlideUrl(url,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + storageManager.getAccessToken())
                        .build()
        );

        Glide.with(mContext)
                .load(glideUrl)
                .into(imageView);
    }
}

package com.example.luigidarco.myfit.managers;

import android.content.Context;
import android.util.Log;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class FitnessManager {

    private final String TAG = "MYFITAPP";
    private Context mContext;

    public FitnessManager(Context context) {
        this.mContext = context;
    }

    public enum FitnessData {
        STEPS("/steps"),
        METERS("/meters"),
        CALORIE("/calories"),
        HEART_RATE("/heart_rates");

        public String label;

        private FitnessData(String label) {
            this.label = label;
        }
    }

    public void saveValue(FitnessData type, int value) {
        String url = mContext.getResources().getString(R.string.url_server) + type.label;
        JSONObject object = new JSONObject();
        try {
            object.put("value", value);
            NetworkManager.makePostJsonObjRequest(mContext, url, object, new NetworkCallback() {
                @Override
                public void onSuccess(JSONObject object) {
                    Log.d(TAG, object.toString());
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

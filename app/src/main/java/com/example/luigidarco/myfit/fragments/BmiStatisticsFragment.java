package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.GraphManager;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BmiStatisticsFragment extends Fragment {

    private String TAG = "MYFITAPP";

    private GraphManager graphManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi_statistics, null, false);

        graphManager = new GraphManager(getContext(), view, R.id.chart);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }

    private void loadData() {
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        String url = getResources().getString(R.string.url_server) + "/personal-info";

        NetworkManager.makeGetJsonObjRequest(getContext(), url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray data = object.getJSONArray("data");
                    for (int i = data.length() - 1; i >= 0; i--) {
                        JSONObject obj = data.getJSONObject(i);
                        values.add(new Entry(
                                data.length() - 1 - i,
                                (float) obj.getDouble("bmi")
                        ));
                        labels.add(obj.getString("timestamp"));
                    }
                    graphManager.setData(values, labels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}

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
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private GraphManager stepGraphManager;
    private GraphManager distanceGraphManager;
    private GraphManager calorieGraphManager;
    private GraphManager heartRatesGraphManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        stepGraphManager = new GraphManager(getContext(), view, R.id.statistics_steps);
        distanceGraphManager = new GraphManager(getContext(), view, R.id.statistics_distances);
        calorieGraphManager = new GraphManager(getContext(), view, R.id.statistics_calories);
        heartRatesGraphManager = new GraphManager(getContext(), view, R.id.statistics_heart_rates);

        return view;
    }

    private void loadData(GraphManager graphManager, String table) {
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        String url = getResources().getString(R.string.url_server) + table;

        NetworkManager.makeGetJsonObjRequest(getContext(), url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        values.add(new Entry(
                                i,
                                obj.getInt("value")
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

    @Override
    public void onResume() {
        super.onResume();

        loadData(stepGraphManager, "/steps");
        loadData(calorieGraphManager, "/calories");
        loadData(distanceGraphManager, "/meters");
        loadData(heartRatesGraphManager, "/heart-rates");
    }

}

package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");

    FitnessCard stepCard;
    FitnessCard meterCard;
    FitnessCard calorieCard;
    FitnessCard heartRateCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        stepCard = new FitnessCard(
                "steps",
                view.findViewById(R.id.statistics_steps),
                new LineGraphSeries<>()
        );
        meterCard = new FitnessCard(
                "meters",
                view.findViewById(R.id.statistics_meters),
                new LineGraphSeries<>()
        );
        calorieCard = new FitnessCard(
                "calories",
                view.findViewById(R.id.statistics_calories),
                new LineGraphSeries<>()
        );
        heartRateCard = new FitnessCard(
                "heart-rates",
                view.findViewById(R.id.statistics_heart_rates),
                new LineGraphSeries<>()
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getFitnessData(stepCard);
        getFitnessData(meterCard);
        getFitnessData(calorieCard);
        getFitnessData(heartRateCard);
    }

    private DefaultLabelFormatter defaultLabelFormatter = new DefaultLabelFormatter() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        @Override
        public String formatLabel(double value, boolean isValueX) {
            if (isValueX) {
                return sdf.format(new Date((long) value));
            } else {
                return super.formatLabel(value, isValueX);
            }
        }
    };

    private void getFitnessData(FitnessCard fitnessCard) {
        String label = fitnessCard.label;
        GraphView graphView = fitnessCard.graphView;
        graphView.getViewport().scrollToEnd();
        graphView.getGridLabelRenderer().setLabelFormatter(defaultLabelFormatter);
        //graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        LineGraphSeries lineGraphSeries = fitnessCard.lineGraphSeries;

        graphView.removeAllSeries();

        String url = getResources().getString(R.string.url_server) + "/" + label;

        NetworkManager.makeGetJsonObjRequest(getContext(), url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject element = array.getJSONObject(i);

                        Log.d(TAG, "label: " + label + " => " + element.toString());

                        Date date = dateFormatter.parse(element.getString("timestamp"));
                        DataPoint dataPoint = new DataPoint(
                                date.getTime(),
                                element.getInt("value")
                        );
                        lineGraphSeries.appendData(dataPoint, true, 50);
                    }
                    graphView.addSeries(lineGraphSeries);
                    graphView.getViewport().setScrollable(true);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private class FitnessCard {

        public String label;
        public GraphView graphView;
        public LineGraphSeries<DataPoint> lineGraphSeries;

        private FitnessCard(String label, GraphView graphView, LineGraphSeries<DataPoint> lineGraphSeries) {
            this.label = label;
            this.graphView = graphView;
            this.lineGraphSeries = lineGraphSeries;
        }
    }

}

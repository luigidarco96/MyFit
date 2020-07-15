package com.example.luigidarco.myfit.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.example.luigidarco.myfit.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class GraphManager {

    private static final String TAG = "MYFITAPP";

    private LineChart mChart;
    private Context mContext;
    private ArrayList<String> labels;

    private IAxisValueFormatter formatter = new IAxisValueFormatter() {
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return labels.get((int) value);
        }
    };

    public GraphManager(Context context, View view, int id) {
        mContext = context;

        mChart = view.findViewById(id);
        chartSettings();
    }

    public void chartSettings() {
        mChart.getXAxis().setLabelCount(1);
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
    }

    public void setData(ArrayList<Entry> values, ArrayList<String> labels) {
        if (values.isEmpty()) {
            return;
        }

        this.labels = labels;

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.side_nav_bar);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.invalidate();
        }
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
}

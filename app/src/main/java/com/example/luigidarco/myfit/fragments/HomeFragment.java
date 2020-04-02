package com.example.luigidarco.myfit.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.managers.FitnessManager;
import com.example.luigidarco.myfit.miband.MiBand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private static final String TAG = "MYFITAPP";

    private FitnessManager fitnessManager;

    private TextView deviceName;
    private TextView stepsCounter;
    private ProgressBar stepsProgress;
    private ImageView stepsReload;
    private TextView metersCounter;
    private ProgressBar metersProgress;
    private ImageView metersReload;
    private TextView caloriesCounter;
    private ProgressBar caloriesProgress;
    private ImageView caloriesReload;
    private TextView heartRate;
    private ProgressBar heartRateProgress;
    private ImageView heartRateReload;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner btScanner;
    private BluetoothDevice myDevice;
    private boolean isScanning = false;

    private MiBand miBand;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            miBand = ((MiBand.LocalBinder) service).getService();
            Log.d(TAG, "Service connected");

            getInformation();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            miBand = null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fitness Manager
        fitnessManager = new FitnessManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Bluetooth Initialisation
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        btScanner = bluetoothAdapter.getBluetoothLeScanner();

        deviceName = root.findViewById(R.id.deviceName);
        stepsCounter = root.findViewById(R.id.stepCount);
        stepsProgress = root.findViewById(R.id.stepsProgress);
        stepsReload = root.findViewById(R.id.stepsReload);
        metersCounter = root.findViewById(R.id.metersCount);
        metersProgress = root.findViewById(R.id.metersProgress);
        metersReload = root.findViewById(R.id.metersReload);
        caloriesCounter = root.findViewById(R.id.caloriesCount);
        caloriesProgress = root.findViewById(R.id.caloriesProgress);
        caloriesReload = root.findViewById(R.id.caloriesReload);
        heartRate = root.findViewById(R.id.heartRate);
        heartRateProgress = root.findViewById(R.id.heartRateProgress);
        heartRateReload = root.findViewById(R.id.heartRateReload);

        stepsReload.setOnClickListener(view -> getSteps());
        metersReload.setOnClickListener(view -> getSteps());
        caloriesReload.setOnClickListener(view -> getSteps());
        heartRateReload.setOnClickListener(view -> getHeartRate());

        /*
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Device not connected")
                .setPositiveButton("Connect now", handleConnection)
                .show();
         */

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("My Fit");
        // Bind service
        Intent gattServiceIntent = new Intent(getActivity(), MiBand.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
    }

    private void getInformation() {
        miBand.enableRealtimeStepsNotify();
        getDeviceName();
        getSteps();
        getHeartRate();
    }

    private void sendAlert() {
        /*
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                miBand.sendAlert();
                sendAlert();
            }
        }, 5000);

         */
    }

    private void sendMessage() {
        /*
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                miBand.sendCustomMessage("Ciao");
                sendMessage();
            }
        }, 5000);

         */
    }

    private void getDeviceName() {
        BluetoothDevice device = miBand.getDevice();
        final String name = device.getName();
        Log.d(TAG, "Device " + name + ": " + device.toString());
        getActivity().runOnUiThread(() -> {
            deviceName.setText(name);
        });
    }

    private void getSteps() {
        hideElement(stepsCounter, stepsProgress);
        hideElement(metersCounter, metersProgress);
        hideElement(caloriesCounter, caloriesProgress);

        hideSingle(stepsReload);
        hideSingle(metersReload);
        hideSingle(caloriesReload);

        Log.d(TAG, "Get steps");
        miBand.setRealtimeStepsNotifyListener(steps -> {
            fitnessManager.saveValue(FitnessManager.FitnessData.STEPS, steps[0]);
            fitnessManager.saveValue(FitnessManager.FitnessData.METERS, steps[1]);
            fitnessManager.saveValue(FitnessManager.FitnessData.CALORIE, steps[2]);

            getActivity().runOnUiThread(() -> {
                // STEP
                stepsCounter.setText(steps[0] + "");
                hideElement(stepsProgress, stepsCounter);
                showSingle(stepsReload);
                // METER
                metersCounter.setText(steps[1] + " m");
                hideElement(metersProgress, metersCounter);
                showSingle(metersReload);
                //CALORIE
                caloriesCounter.setText(steps[2] + " cal");
                hideElement(caloriesProgress, caloriesCounter);
                showSingle(caloriesReload);
            });
        });
    }

    private void getHeartRate() {
        hideElement(heartRate, heartRateProgress);
        hideSingle(heartRateReload);

        Log.d(TAG, "Get HeartRate");
        miBand.setHeartRateScanListener((heartrate) -> {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    heartRate.setText(heartrate + " bpm");
                    hideElement(heartRateProgress, heartRate);
                    showSingle(heartRateReload);
                }
            });
        });
        miBand.startHeartRateScan();
        new Handler().postDelayed(() -> {
            heartRate.setText("--");
            hideElement(heartRateProgress, heartRate);
        }, 20000);
    }

    private void hideElement(View v1, View v2) {
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.VISIBLE);
    }

    private void hideSingle(View v1) {
        v1.setVisibility(View.GONE);
    }

    private void showSingle(View v1) {
        v1.setVisibility(View.VISIBLE);
    }
}

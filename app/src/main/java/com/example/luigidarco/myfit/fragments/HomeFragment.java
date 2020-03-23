package com.example.luigidarco.myfit.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.luigidarco.myfit.MainActivity;
import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.miband.ActionCallback;
import com.example.luigidarco.myfit.miband.MiBand;
import com.example.luigidarco.myfit.miband.listeners.HeartRateNotifyListener;
import com.example.luigidarco.myfit.miband.listeners.RealtimeStepsNotifyListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private static final String TAG = "MYFITAPP";

    //Change with dynamic variable
    private String macAddress = "ED:06:99:FD:16:46";

    private TextView deviceName;
    private TextView stepsCounter;
    private ProgressBar stepsProgress;
    private TextView metersCounter;
    private ProgressBar metersProgress;
    private TextView caloriesCounter;
    private ProgressBar caloriesProgress;
    private TextView heartRate;
    private ProgressBar heartRateProgress;

    private Handler mHandler;
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

        Intent gattServiceIntent = new Intent(getActivity(), MiBand.class);
        getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mHandler = new Handler();

        //Bluetooth Initialisation
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        btScanner = bluetoothAdapter.getBluetoothLeScanner();

        stepsCounter = root.findViewById(R.id.stepCount);
        stepsProgress = root.findViewById(R.id.stepsProgress);
        metersCounter = root.findViewById(R.id.metersCount);
        metersProgress = root.findViewById(R.id.metersProgress);
        caloriesCounter = root.findViewById(R.id.caloriesCount);
        caloriesProgress = root.findViewById(R.id.caloriesProgress);
        heartRate = root.findViewById(R.id.heartRate);
        heartRateProgress = root.findViewById(R.id.heartRateProgress);

        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Device not connected")
                .setPositiveButton("Connect now", handleConnection)
                .show();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("My Fit");
    }

    private void getInformation() {
        // getDeviceName();
        getSteps();
        getHeartRate();

        //sendMessage();
        //sendAlert();
    }

    private void sendAlert() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                miBand.sendAlert();
                sendAlert();
            }
        }, 5000);
    }

    private void sendMessage() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                miBand.sendCustomMessage("Ciao");
                sendMessage();
            }
        }, 5000);
    }

    private void getDeviceName() {
        BluetoothDevice device = miBand.getDevice();
        final String name = device.getName();
        Log.d(TAG, "Device " + name + ": " + device.toString());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceName.setText(name);
            }
        });
    }

    public void getBatteryInfo() {
        miBand.getBatteryInfo(new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.d(TAG, "Battery info:");
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                Log.d(TAG, "Battery fail: " + msg);
            }
        });
    }

    private void getSteps() {
        hideElement(stepsCounter, stepsProgress);
        hideElement(metersCounter, metersProgress);
        hideElement(caloriesCounter, caloriesProgress);

        Log.d(TAG, "Get steps");
        miBand.setRealtimeStepsNotifyListener(new RealtimeStepsNotifyListener() {

            @Override
            public void onNotify(final int[] steps) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stepsCounter.setText("" + steps[0]);
                        metersCounter.setText("" + steps[1]);
                        caloriesCounter.setText("" + steps[2]);

                        hideElement(stepsProgress, stepsCounter);
                        hideElement(metersProgress, metersCounter);
                        hideElement(caloriesProgress, caloriesCounter);
                    }
                });
            }
        });
        miBand.enableRealtimeStepsNotify();
    }

    private void getHeartRate() {
        hideElement(heartRate, heartRateProgress);

        Log.d(TAG, "Get HeartRate");
        miBand.setHeartRateScanListener(new HeartRateNotifyListener() {
            @Override
            public void onNotify(final int heartrate) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        heartRate.setText(heartrate + " bpm");
                        hideElement(heartRateProgress, heartRate);
                    }
                });
            }
        });
        miBand.startHeartRateScan();
    }

    private void hideElement(View v1, View v2) {
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.VISIBLE);
    }

    private DialogInterface.OnClickListener handleConnection = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isScanning) {
                        Log.d(TAG, "Force Stop Scanning");
                        btScanner.stopScan(scanCallback);
                        // progressDialog.cancel();
                    }
                }
            }, 10000);

            Log.d(TAG, "Start Scanning");
            btScanner.startScan(scanCallback);
            isScanning = true;
            // progressDialog.show();
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            final BluetoothDevice device = result.getDevice();

            if (device.getAddress().equals(macAddress)) {
                Log.d(TAG, "Device found");
                btScanner.stopScan(scanCallback);
                isScanning = false;
                miBand.connect(device, new ActionCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d(TAG, "Connected");

                        // Intent intent = new Intent(SyncDeviceActivity.this, MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // startActivity(intent);
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {
                        Log.e(TAG, "Connection failed. Error: " + msg);
                    }
                });
            }
        }
    };
}

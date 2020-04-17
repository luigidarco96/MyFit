package com.example.luigidarco.myfit;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.luigidarco.myfit.adapters.BleListAdapter;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.miband.ActionCallback;
import com.example.luigidarco.myfit.miband.MiBand;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class BluetoothDeviceListActivity extends AppCompatActivity {

    private final String TAG = "MYFITAPP";

    private StorageManager storageManager;

    ArrayList<BluetoothDevice> devices;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner btScanner;
    private Handler mHandler;

    private RecyclerView recyclerView;
    private BleListAdapter bleListAdapter;

    private ProgressDialog progressDialog;

    private boolean isScanning = false;

    private MiBand miBand;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            miBand = ((MiBand.LocalBinder) service).getService();
            Log.d(TAG, "Service connected");

            String myDevice = storageManager.getDevice();

            // Auto-reconnect
            if (!myDevice.equals("")) {
                Log.d(TAG, "Device already exist: " + myDevice);
                BluetoothDevice mBluetoothDevice = bluetoothManager.getAdapter().getRemoteDevice(myDevice);
                handleConnection(mBluetoothDevice, false);
            } else {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            miBand = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        miBand = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_devices_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.header_ble_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        storageManager = new StorageManager(this);

        // Bind Service
        Intent gattServiceIntent = new Intent(BluetoothDeviceListActivity.this, MiBand.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        recyclerView = findViewById(R.id.ble_list);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        devices = new ArrayList<>();

        bleListAdapter = new BleListAdapter(devices, getApplicationContext(), itemClick -> {
            handleConnection(itemClick, true);
        });
        recyclerView.setAdapter(bleListAdapter);

        // Initialise BLE
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        btScanner = bluetoothAdapter.getBluetoothLeScanner();
        mHandler = new Handler();

        startScanning();
    }

    private void handleConnection(BluetoothDevice device, boolean newDevice) {

        Log.d(TAG, "Connection...");
        ProgressDialog connectionProgress = new ProgressDialog(this);
        connectionProgress.setMessage("Connection...");
        connectionProgress.setCancelable(false);
        connectionProgress.show();
        miBand.connect(device, new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                if (isScanning) {
                    btScanner.stopScan(scanCallback);
                    isScanning = false;
                }
                if (newDevice) {
                    runOnUiThread(() -> {
                        connectionProgress.setMessage("Check your device");
                    });
                    miBand.pair(new ActionCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            storageManager.setDevice(device.getAddress());
                            Intent intent = new Intent(BluetoothDeviceListActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(int errorCode, String msg) {
                            runOnUiThread(() -> {
                                connectionProgress.dismiss();
                                new MaterialAlertDialogBuilder(BluetoothDeviceListActivity.this)
                                        .setTitle("Connection failed")
                                        .setMessage("Please try again")
                                        .show();
                            });
                            Log.d(TAG, "Error: " + msg);
                        }
                    });
                } else {
                    Intent intent = new Intent(BluetoothDeviceListActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                runOnUiThread(() -> {
                    connectionProgress.dismiss();
                    new MaterialAlertDialogBuilder(BluetoothDeviceListActivity.this)
                            .setTitle("Connection failed")
                            .setMessage("Please try again")
                            .show();
                });
                Log.d(TAG, "Error: " + msg);
            }
        });
    }

    public void startScanning() {
        mHandler.postDelayed(() -> {
            if (isScanning) {
                Log.d(TAG, "Force Stop Scanning");
                btScanner.stopScan(scanCallback);
                isScanning = false;
                // progressDialog.cancel();
            }
        }, 100000);

        Log.d(TAG, "Start Scanning");
        btScanner.startScan(scanCallback);
        isScanning = true;
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            final BluetoothDevice device = result.getDevice();
            if (!devices.contains(device)) {
                devices.add(device);
                bleListAdapter.notifyDataSetChanged();
            }
        }
    };
}

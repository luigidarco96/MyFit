package com.example.luigidarco.myfit.managers;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import com.example.luigidarco.myfit.adapters.BleListAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    final static int LOCATION_CODE = 22;
    final static int BLUETOOTH_CODE = 33;
    final static int CAMERA_CODE = 44;

    /* ============== LOCATION ============== */
    public static boolean checkLocationUsage(Activity act) {
        if (!checkLocation(act)) {
            requestLocation(act, LOCATION_CODE);
            return false;
        } else if (!isLocationEnabled(act)) {
            new MaterialAlertDialogBuilder(act)
                    .setTitle("GPS required")
                    .setMessage("Please enable it")
                    .setCancelable(false)
                    .setPositiveButton("Go to settings", (dialogInterface, i) -> {
                        act.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("Exit", (dialogInterface, i) -> {
                        act.finish();
                    })
                    .show();
            return false;
        } else {
            return true;
        }
    }

    public static void requestLocation(Activity act,int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
    }

    public static boolean checkLocation(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isLocationEnabled(Activity act) {
        LocationManager lm = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = false;
        enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
    }

    /* ============== CAMERA ============== */

    public static void requestCamera(Activity act) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
    }

    public static void requestCamera(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.CAMERA}, code);
    }

    public static boolean checkCamera(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /* ============== BLUETOOTH ============== */

    public static boolean isBluetoothEnabled(Activity act) {
        BluetoothManager bluetoothManager = (BluetoothManager) act.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            act.startActivityForResult(enableBtIntent, BLUETOOTH_CODE);
            return false;
        } else {
            return true;
        }
    }

    /* ============== NETWORK ============== */

    public static boolean isNetworkEnabled(Activity act) {

        return false;
    }
}

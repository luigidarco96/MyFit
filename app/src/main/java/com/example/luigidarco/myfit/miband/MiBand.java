package com.example.luigidarco.myfit.miband;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.luigidarco.myfit.miband.listeners.HeartRateNotifyListener;
import com.example.luigidarco.myfit.miband.listeners.NotifyListener;
import com.example.luigidarco.myfit.miband.listeners.RealtimeStepsNotifyListener;
import com.example.luigidarco.myfit.miband.modules.BatteryInfo;
import com.example.luigidarco.myfit.miband.modules.Profile;
import com.example.luigidarco.myfit.miband.modules.Protocol;
import com.example.luigidarco.myfit.miband.modules.StepsInfo;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.Nullable;

public class MiBand extends Service {

    private static final String TAG = "MYFITAPP";

    IBinder mBinder = new LocalBinder();

    private Context context;
    private BluetoothIO io;

    public MiBand() {
        this.context = this;
        this.io = new BluetoothIO();
    }

    public static void startScan(ScanCallback callback) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter) {
            Log.e(TAG, "BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (null == scanner) {
            Log.e(TAG, "BluetoothLeScanner is null");
            return;
        }
        scanner.startScan(callback);
    }

    public static void stopScan(ScanCallback callback) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter) {
            Log.e(TAG, "BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (null == scanner) {
            Log.e(TAG, "BluetoothLeScanner is null");
            return;
        }
        scanner.stopScan(callback);
    }

    public void connect(BluetoothDevice device, final ActionCallback callback) {
        this.io.connect(context, device, callback);
    }

    public void close() {
        this.io.close();
    }

    public void setDisconnectedListener(NotifyListener disconnectedListener) {
        this.io.setDisconnectedListener(disconnectedListener);
    }

    public void pair(final ActionCallback callback) {

        setAuthNotifyListener(value -> {
            if (value[0] == 0x10 && value[1] == 0x01 && value[2] == 0x01) {
                this.io.writeCharacteristic(Profile.UUID_SERVICE_BAND_2, Profile.UUID_CHAR_AUTH, Protocol.PAIR_2, null);
            } else if (value[0] == 0x10 && value[1] == 0x02 && value[2] == 0x01) {
                try {
                    byte[] tmpValue = Arrays.copyOfRange(value, 3, 19);
                    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

                    SecretKeySpec key = new SecretKeySpec(Protocol.PAIR_SECRET, "AES");

                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    byte[] bytes = cipher.doFinal(tmpValue);

                    byte[] rq = ArrayUtils.addAll(Protocol.PAIR_3, bytes);
                    this.io.writeCharacteristic(Profile.UUID_SERVICE_BAND_2, Profile.UUID_CHAR_AUTH, rq, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(-1, "Handshake failed - 2 step");
                }
            } else if (value[0] == 0x10 && value[1] == 0x03 && value[2] == 0x01) {
                callback.onSuccess(null);
            } else {
                callback.onFailure(-1, "Handshake failed");
            }
        });

        this.io.writeCharacteristic(Profile.UUID_SERVICE_BAND_2, Profile.UUID_CHAR_AUTH, Protocol.PAIR, null);

    }

    public void setAuthNotifyListener(final NotifyListener listener) {
        this.io.setAuthListener(Profile.UUID_SERVICE_BAND_2, Profile.UUID_CHAR_AUTH, data -> {
            listener.onNotify(data);
        });
    }

    public BluetoothDevice getDevice() {
        return this.io.getDevice();
    }

    public void getBatteryInfo(final ActionCallback callback) {
        ActionCallback ioCallback = new ActionCallback() {

            @Override
            public void onSuccess(Object data) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
                Log.d(TAG, "getBatteryInfo result " + Arrays.toString(characteristic.getValue()));
                if (characteristic.getValue().length == 10) {
                    BatteryInfo info = BatteryInfo.fromByteData(characteristic.getValue());
                    callback.onSuccess(info);
                } else {
                    callback.onFailure(-1, "result format wrong!");
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                callback.onFailure(errorCode, msg);
            }
        };
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.io.readCharacteristic(Profile.UUID_CHAR_BATTERY, ioCallback);
    }

    public void setSensorDataNotifyListener(final NotifyListener listener) {
        this.io.setNotifyListener(Profile.UUID_SERVICE_BAND_1, Profile.UUID_CHAR_SENSOR_DATA, new NotifyListener() {

            @Override
            public void onNotify(byte[] data) {
                listener.onNotify(data);
            }
        });
    }

    public void showServicesAndCharacteristics() {
        for (BluetoothGattService service : this.io.gatt.getServices()) {
            Log.d(TAG, "onServicesDiscovered:" + service.getUuid());

            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                Log.d(TAG, "  char:" + characteristic.getUuid());

                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    Log.d(TAG, "    descriptor:" + descriptor.getUuid());
                }
            }
        }
    }

    public void setRealtimeStepsNotifyListener(final RealtimeStepsNotifyListener listener) {
        this.io.setNotifyListener(Profile.UUID_SERVICE_BAND_1, Profile.UUID_CHAR_STEPS, new NotifyListener() {

            @Override
            public void onNotify(byte[] data) {
                int steps = StepsInfo.getSteps(data);
                int meters = StepsInfo.getMeters(data);
                int calories = StepsInfo.getCalories(data);
                int[] list = {steps, meters, calories};
                if (steps != -1) {
                    listener.onNotify(list);
                }
            }
        });
    }

    public void enableRealtimeStepsNotify() {
        this.io.writeCharacteristic(Profile.UUID_CHAR_CONTROL_POINT, Protocol.ENABLE_REALTIME_STEPS_NOTIFY, null);
    }

    public void setHeartRateScanListener(final HeartRateNotifyListener listener) {
        this.io.setNotifyListener(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CHAR, new NotifyListener() {
            @Override
            public void onNotify(byte[] data) {
                if (data.length == 2) {
                    int heartRate = data[1] & 0xFF;
                    listener.onNotify(heartRate);
                }
            }
        });
    }

    public void startHeartRateScan() {
        try {
            //MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CONTROL, Protocol.START_HEART_RATE_SCAN, null);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CONTROL, new byte[]{0x15, 0x02, 0x00}, null);
            Thread.sleep(1000);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CONTROL, new byte[]{0x15, 0x01, 0x00}, null);
            Thread.sleep(1000);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CHAR, new byte[]{0x01, 0x03, 0x19}, null);
            Thread.sleep(1000);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_DESCRIPTOR, new byte[]{0x01, 0x00}, null);
            Thread.sleep(1000);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CONTROL, new byte[]{0x15, 0x01, 0x01}, null);
            Thread.sleep(1000);
            MiBand.this.io.writeCharacteristic(Profile.UUID_HEART_SERVICE, Profile.UUID_HEART_CHAR, new byte[]{0x02}, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendAlert() {
        this.io.writeCharacteristic(Profile.UUID_ALERT_SERVICE, Profile.UUID_ALERT_CHAR, new byte[]{0x01}, null);
    }

    public void sendCustomMessage(String message) {
        String value = "Luigi";
        this.io.writeCharacteristic(Profile.UUID_ALERT_NOTIFICATION_SERVICE, Profile.UUID_CUSTOM_ALERT_CHAR, value, null);
    }

    public void enableSensorDataNotify() {
        this.io.writeCharacteristic(Profile.UUID_CHAR_CONTROL_POINT, Protocol.ENABLE_SENSOR_DATA_NOTIFY, null);
    }

    public void disableSensorDataNotify() {
        this.io.writeCharacteristic(Profile.UUID_CHAR_CONTROL_POINT, Protocol.DISABLE_SENSOR_DATA_NOTIFY, null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MiBand getService() {
            return MiBand.this;
        }
    }
}

package com.example.luigidarco.myfit.miband;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.example.luigidarco.myfit.miband.listeners.NotifyListener;
import com.example.luigidarco.myfit.miband.modules.Profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothIO extends BluetoothGattCallback implements Serializable {

    private final String TAG = "MiBand";

    public BluetoothGatt gatt;
    private ActionCallback currentCallback;

    private HashMap<UUID, NotifyListener> notifyListeners = new HashMap<>();
    private NotifyListener disconnectedListener = null;

    public void connect(final Context context, BluetoothDevice device, final ActionCallback callback) {
        BluetoothIO.this.currentCallback = callback;
        device.connectGatt(context, true, BluetoothIO.this);
    }

    public void close() {
        gatt.close();
    }

    public void setDisconnectedListener(NotifyListener disconnectedListener) {
        this.disconnectedListener = disconnectedListener;
    }

    public BluetoothDevice getDevice() {
        if (gatt == null) {
            Log.e(TAG, "Connect to miband first");
            return null;
        }
        return gatt.getDevice();
    }

    public void writeAndRead(final UUID uuid, byte[] valueToWrite, final ActionCallback callback) {
        ActionCallback readCallback = new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                BluetoothIO.this.readCharacteristic(uuid, callback);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                callback.onFailure(errorCode, msg);
            }
        };
        this.writeCharacteristic(uuid, valueToWrite, readCallback);
    }

    public void writeCharacteristic(UUID characteristic, byte[] value, ActionCallback callback) {
        writeCharacteristic(Profile.UUID_SERVICE_BAND_1, characteristic, value, callback);
    }

    public void writeCharacteristic(UUID service, UUID characteristic, byte[] value, ActionCallback callback) {
        if (gatt == null) {
            Log.e(TAG, "Connect to miband first");
            return;
        }
        this.currentCallback = callback;
        BluetoothGattCharacteristic chara = gatt.getService(service).getCharacteristic(characteristic);
        if (chara == null) {
            this.onFailure(-1, "BluetoothGattCharacteristic " + characteristic + " does not exist");
            return;
        }
        chara.setValue(value);
        if (!gatt.writeCharacteristic(chara)) {
            this.onFailure(-1, "gatt.writeCharacteristic fail");
        }
    }

    public void writeCharacteristic(UUID service, UUID characteristic, String value, ActionCallback callback) {
        if (gatt == null) {
            Log.e(TAG, "Connect to miband first");
            return;
        }
        this.currentCallback = callback;
        BluetoothGattCharacteristic chara = gatt.getService(service).getCharacteristic(characteristic);
        if (chara == null) {
            this.onFailure(-1, "BluetoothGattCharacteristic " + characteristic + " does not exist");
            return;
        }
        chara.setValue(value);
        if (!gatt.writeCharacteristic(chara)) {
            this.onFailure(-1, "gatt.writeCharacteristic fail");
        }
    }

    public void readCharacteristic(UUID characteristic, ActionCallback callback) {
        this.readCharacteristic(Profile.UUID_SERVICE_BAND_1, characteristic, callback);
    }

    public void readCharacteristic(UUID service, UUID characteristic, ActionCallback callback) {
        if (gatt == null) {
            Log.e(TAG, "Connect to miband first");
            return;
        }
        this.currentCallback = callback;
        BluetoothGattCharacteristic chara = gatt.getService(service).getCharacteristic(characteristic);
        if (chara == null) {
            this.onFailure(-1, "BluetoothGattCharacteristic " + characteristic + " does not exist");
        }
        if (!gatt.readCharacteristic(chara)) {
            this.onFailure(-1, "gatt.readCharacteristic() fail");
        }
    }

    public void setNotifyListener(UUID service, UUID characteristic, NotifyListener listener) {
        if (gatt == null) {
            Log.e(TAG, "Connect to miband first");
            return;
        }
        BluetoothGattCharacteristic chara = gatt.getService(service).getCharacteristic(characteristic);
        if (chara == null) {
            Log.e(TAG, "Characterstic " + characteristic + " not found in service " + service);
            return;
        }
        gatt.setCharacteristicNotification(chara, true);
        BluetoothGattDescriptor descriptor = chara.getDescriptor(Profile.UUID_DESCRIPTOR_UPDATE_NOTIFICATION);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
        this.notifyListeners.put(characteristic, listener);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            gatt.close();
            if (this.disconnectedListener != null)
                this.disconnectedListener.onNotify(null);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        //super.onCharacteristicRead(gatt, characteristic, status);
        Log.d(TAG, "Read: " + characteristic.getUuid().toString() + " status: " + status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            this.onSuccess(characteristic);
        } else {
            this.onFailure(status, "onCharacteristicRead fail");
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            this.onSuccess(characteristic);
        } else {
            this.onFailure(status, "onCharacteristicWrite fail");
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            this.gatt = gatt;
            this.onSuccess(null);
        } else {
            this.onFailure(status, "onServicesDiscovered fail");
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        if (this.notifyListeners.containsKey(characteristic.getUuid())) {
            this.notifyListeners.get(characteristic.getUuid()).onNotify(characteristic.getValue());
        }
    }

    private void onSuccess(Object data) {
        if (this.currentCallback != null) {
            ActionCallback callback = this.currentCallback;
            this.currentCallback = null;
            callback.onSuccess(data);
        }
    }

    private void onFailure(int errorCode, String msg) {
        if (this.currentCallback != null) {
            ActionCallback callback = this.currentCallback;
            this.currentCallback = null;
            callback.onFailure(errorCode, msg);
        }
    }
}

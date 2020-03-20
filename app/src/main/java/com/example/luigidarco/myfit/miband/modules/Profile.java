package com.example.luigidarco.myfit.miband.modules;

import java.util.UUID;

public class Profile {

    public static final UUID UUID_SERVICE_BAND_1 = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_DESCRIPTOR_UPDATE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_PAIR = UUID.fromString("0000ff0f-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_BATTERY = UUID.fromString("00000006-0000-3512-2118-0009af100700");//UUID.fromString("0000ff0c-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_SENSOR_DATA = UUID.fromString("0000ff0e-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_CONTROL_POINT = UUID.fromString("0000ff05-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHAR_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");

    public static final UUID UUID_HEART_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_HEART_CHAR = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_HEART_DESCRIPTOR = convertFromInteger(0x2902);
    public static final UUID UUID_HEART_CONTROL = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_ALERT_NOTIFICATION_SERVICE = UUID.fromString("00001811-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CUSTOM_ALERT_CHAR = UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_ALERT_SERVICE = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_ALERT_CHAR = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    private static UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

}

package com.example.luigidarco.myfit.miband.modules;

public class Protocol {

    public static final byte[] PAIR = { 0x01, 0x8, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45 };
    public static final byte[] PAIR_2 = { 0x02, 0x08};
    public static final byte[] PAIR_3 = {0x03, 0x8};
    public static final byte[] PAIR_SECRET = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45};

    public static final byte[] ENABLE_SENSOR_DATA_NOTIFY = {18, 1};
    public static final byte[] DISABLE_SENSOR_DATA_NOTIFY = {18, 0};
    public static final byte[] ENABLE_REALTIME_STEPS_NOTIFY = {3, 1};

    public static final byte[] START_HEART_RATE_SCAN = {15, 01, 00};

    public static final byte[] CUSTOM_ALERT_1 = {0x05, 0x01};
    public static final byte[] CUSTOM_ALERT_2 = {0x04, 0x01};
    public static final byte[] CUSTOM_ALERT_3 = {0x03, 0x01};
}

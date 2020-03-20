package com.example.luigidarco.myfit.miband.modules;

public class Protocol {

    public static final byte[] PAIR = {2};
    public static final byte[] ENABLE_SENSOR_DATA_NOTIFY = {18, 1};
    public static final byte[] DISABLE_SENSOR_DATA_NOTIFY = {18, 0};
    public static final byte[] ENABLE_REALTIME_STEPS_NOTIFY = {3, 1};

    public static final byte[] START_HEART_RATE_SCAN = {15, 01, 00};

    public static final byte[] CUSTOM_ALERT_1 = {0x05, 0x01};
    public static final byte[] CUSTOM_ALERT_2 = {0x04, 0x01};
    public static final byte[] CUSTOM_ALERT_3 = {0x03, 0x01};
}

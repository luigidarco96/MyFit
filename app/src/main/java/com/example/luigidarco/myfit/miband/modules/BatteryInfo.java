package com.example.luigidarco.myfit.miband.modules;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BatteryInfo {

    static enum Status {
        UNKNOWN, LOW, FULL, CHARGING, NOT_CHARGING;

        public static Status fromByte(byte b) {
            switch (b) {
                case 1:
                    return LOW;
                case 2:
                    return CHARGING;
                case 3:
                    return FULL;
                case 4:
                    return NOT_CHARGING;

                default:
                    return UNKNOWN;
            }
        }
    }

    private int level;
    private int cycles;
    private Status status;
    private Calendar lastChargedDate;

    private BatteryInfo() {}

    public static BatteryInfo fromByteData(byte[] data) {
        if (data.length < 10) {
            return null;
        }
        BatteryInfo info = new BatteryInfo();

        info.level = data[0];
        info.status = Status.fromByte(data[9]);
        info.cycles = 0xffff & (0xff & data[7] | (0xff & data[8]) << 8);
        info.lastChargedDate = Calendar.getInstance();

        info.lastChargedDate.set(Calendar.YEAR, data[1] + 2000);
        info.lastChargedDate.set(Calendar.MONTH, data[2]);
        info.lastChargedDate.set(Calendar.DATE, data[3]);

        info.lastChargedDate.set(Calendar.HOUR_OF_DAY, data[4]);
        info.lastChargedDate.set(Calendar.MINUTE, data[5]);
        info.lastChargedDate.set(Calendar.SECOND, data[6]);

        return info;
    }

    public int getLevel() {
        return level;
    }

    public int getCycles() {
        return cycles;
    }

    public Status getStatus() {
        return status;
    }

    public Calendar getLastChargedDate() {
        return lastChargedDate;
    }

    public String toString() {
        return "cycles:" + this.getCycles()
                + ",level:" + this.getLevel()
                + ",status:" + this.getStatus()
                + ",last:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.UK).format(this.getLastChargedDate().getTime());
    }
}

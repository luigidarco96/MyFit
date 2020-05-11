package com.example.luigidarco.myfit.miband.modules;

public class StepsInfo {

    public static int getSteps(byte[] value) {
        if (value != null && value.length == 13) {
            byte[] stepsValue = new byte[] {value[1], value[2]};
            int steps = byteToUint16(stepsValue);
            return steps;
        }
        return -1;
    }

    public static int getMeters(byte[] value) {
        if (value != null && value.length == 13) {
            byte[] stepsValue = new byte[] {value[5], value[6]};
            int meters = byteToUint16(stepsValue);
            return meters;
        }
        return -1;
    }

    public static int getCalories(byte[] value) {
        if (value != null && value.length == 13) {
            int calorie = byteToUint16(value[9]);
            return calorie;
        }
        return -1;
    }

    public static int toUnsigned(byte value) {
        return value & 0xff;
    }

    public static int byteToUint16(byte value) {
        return toUnsigned(value);
    }

    private static int byteToUint16(byte... bytes) {
        return (bytes[0] & 0xff) | ((bytes[1] & 0xff) << 8);
    }
}

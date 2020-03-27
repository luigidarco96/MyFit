package com.example.luigidarco.myfit.models;

public class BleDevice {

    private String name;
    private String macAddress;

    public BleDevice() {}

    public BleDevice(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}

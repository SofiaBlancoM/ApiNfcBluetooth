package com.sofi.apinfcbluetooth.domain.device.port;

public interface BluetoothAvailabilityPort {
    boolean isSupported();
    boolean isEnabled();
}
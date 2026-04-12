package com.sofi.apinfcbluetooth.domain.device.port;

public interface NfcAvailabilityPort {
    boolean isSupported();
    boolean isEnabled();
}
package com.sofi.apinfcbluetooth.infrastructure.device.nfc;

import com.sofi.apinfcbluetooth.domain.device.port.NfcAvailabilityPort;

public class MockNfcAvailabilityAdapter implements NfcAvailabilityPort {

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
package com.sofi.apinfcbluetooth.application.device.usecase;

import com.sofi.apinfcbluetooth.domain.device.model.DeviceAvailability;
import com.sofi.apinfcbluetooth.domain.device.port.NfcAvailabilityPort;

public class CheckNfcAvailabilityUseCase {

    private final NfcAvailabilityPort port;

    public CheckNfcAvailabilityUseCase(NfcAvailabilityPort port) {
        this.port = port;
    }

    public DeviceAvailability execute() {
        if (!port.isSupported()) {
            return DeviceAvailability.NOT_SUPPORTED;
        }

        if (!port.isEnabled()) {
            return DeviceAvailability.DISABLED;
        }

        return DeviceAvailability.READY;
    }
}
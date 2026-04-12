package com.sofi.apinfcbluetooth.application.device.usecase;

import com.sofi.apinfcbluetooth.domain.device.model.DeviceAvailability;
import com.sofi.apinfcbluetooth.domain.device.port.BluetoothAvailabilityPort;

public class CheckBluetoothAvailabilityUseCase {

    private final BluetoothAvailabilityPort port;

    public CheckBluetoothAvailabilityUseCase(BluetoothAvailabilityPort port) {
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

package com.sofi.apinfcbluetooth.infrastructure.device.bluetooth;

import android.bluetooth.BluetoothAdapter;

import com.sofi.apinfcbluetooth.domain.device.port.BluetoothAvailabilityPort;

public class AndroidBluetoothAvailabilityAdapter implements BluetoothAvailabilityPort {

    private final BluetoothAdapter adapter;

    public AndroidBluetoothAvailabilityAdapter() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean isSupported() {
        return adapter != null;
    }

    @Override
    public boolean isEnabled() {
        return adapter != null && adapter.isEnabled();
    }
}

package com.sofi.apinfcbluetooth.application.device.usecase;

import com.sofi.apinfcbluetooth.domain.device.model.BluetoothDevice;
import com.sofi.apinfcbluetooth.domain.device.port.BluetoothDeviceReader;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

import java.util.List;

public class GetBondedBluetoothDevicesUseCase {

    private final BluetoothDeviceReader reader;

    public GetBondedBluetoothDevicesUseCase(BluetoothDeviceReader reader) {
        this.reader = reader;
    }

    public AppResult<List<BluetoothDevice>> execute() {
        return reader.getBondedDevices();
    }
}

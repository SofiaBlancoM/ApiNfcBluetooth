package com.sofi.apinfcbluetooth.domain.device.port;

import com.sofi.apinfcbluetooth.domain.device.model.BluetoothDevice;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

import java.util.List;

public interface BluetoothDeviceReader {
    AppResult<List<BluetoothDevice>> getBondedDevices();
}

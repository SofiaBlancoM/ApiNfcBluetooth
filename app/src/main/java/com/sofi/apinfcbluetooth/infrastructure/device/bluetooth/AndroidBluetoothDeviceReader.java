package com.sofi.apinfcbluetooth.infrastructure.device.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;

import androidx.annotation.RequiresPermission;

import com.sofi.apinfcbluetooth.domain.device.model.BluetoothDevice;
import com.sofi.apinfcbluetooth.domain.device.port.BluetoothDeviceReader;
import com.sofi.apinfcbluetooth.domain.device.result.AppError;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AndroidBluetoothDeviceReader implements BluetoothDeviceReader {

    private final BluetoothAdapter adapter;

    public AndroidBluetoothDeviceReader() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    public AppResult<List<BluetoothDevice>> getBondedDevices() {
        try {
            if (adapter == null) {
                return new AppResult.Failure<>(AppError.BLUETOOTH_NOT_SUPPORTED);
            }

            if (!adapter.isEnabled()) {
                return new AppResult.Failure<>(AppError.BLUETOOTH_DISABLED);
            }

            Set<android.bluetooth.BluetoothDevice> bondedDevices = adapter.getBondedDevices();
            List<BluetoothDevice> result = new ArrayList<>();

            if (bondedDevices != null) {
                for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                    String name = device.getName() != null ? device.getName() : "Unknown";
                    String address = device.getAddress() != null ? device.getAddress() : "";
                    result.add(new BluetoothDevice(name, address));
                }
            }

            return new AppResult.Success<>(result);
        } catch (Exception ex) {
            return new AppResult.Failure<>(AppError.UNKNOWN);
        }
    }
}

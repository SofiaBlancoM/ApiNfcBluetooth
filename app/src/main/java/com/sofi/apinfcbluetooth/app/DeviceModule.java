package com.sofi.apinfcbluetooth.app;

import android.content.Context;

import com.sofi.apinfcbluetooth.application.device.service.DeviceErrorMapper;
import com.sofi.apinfcbluetooth.application.device.usecase.CheckBluetoothAvailabilityUseCase;
import com.sofi.apinfcbluetooth.application.device.usecase.CheckNfcAvailabilityUseCase;
import com.sofi.apinfcbluetooth.application.device.usecase.GetBondedBluetoothDevicesUseCase;
import com.sofi.apinfcbluetooth.application.device.usecase.ReadNfcTagUseCase;
import com.sofi.apinfcbluetooth.domain.device.port.BluetoothAvailabilityPort;
import com.sofi.apinfcbluetooth.domain.device.port.BluetoothDeviceReader;
import com.sofi.apinfcbluetooth.domain.device.port.NfcAvailabilityPort;
import com.sofi.apinfcbluetooth.domain.device.port.NfcTagReader;
import com.sofi.apinfcbluetooth.infrastructure.device.bluetooth.AndroidBluetoothAvailabilityAdapter;
import com.sofi.apinfcbluetooth.infrastructure.device.bluetooth.AndroidBluetoothDeviceReader;
import com.sofi.apinfcbluetooth.infrastructure.device.nfc.AndroidNfcAvailabilityAdapter;
import com.sofi.apinfcbluetooth.infrastructure.device.nfc.AndroidNfcTagReader;
import com.sofi.apinfcbluetooth.infrastructure.device.nfc.MockNfcAvailabilityAdapter;
import com.sofi.apinfcbluetooth.infrastructure.device.nfc.MockNfcTagReader;
import com.sofi.apinfcbluetooth.presentation.device.DeviceViewModel;

/***
 * Dependency injection module
 */
public class DeviceModule {

    public static DeviceViewModel provideDeviceViewModel(Context context) {
        //Implementación real
        //NfcAvailabilityPort nfcAvailabilityPort = new AndroidNfcAvailabilityAdapter(context);
        //NfcTagReader nfcTagReader = new AndroidNfcTagReader();

        NfcAvailabilityPort nfcAvailabilityPort = new MockNfcAvailabilityAdapter();
        NfcTagReader nfcTagReader = new MockNfcTagReader();

        BluetoothAvailabilityPort bluetoothAvailabilityPort = new AndroidBluetoothAvailabilityAdapter();
        BluetoothDeviceReader bluetoothDeviceReader = new AndroidBluetoothDeviceReader();

        CheckNfcAvailabilityUseCase checkNfcAvailabilityUseCase =
                new CheckNfcAvailabilityUseCase(nfcAvailabilityPort);
        ReadNfcTagUseCase readNfcTagUseCase =
                new ReadNfcTagUseCase(nfcTagReader);

        CheckBluetoothAvailabilityUseCase checkBluetoothAvailabilityUseCase =
                new CheckBluetoothAvailabilityUseCase(bluetoothAvailabilityPort);
        GetBondedBluetoothDevicesUseCase getBondedBluetoothDevicesUseCase =
                new GetBondedBluetoothDevicesUseCase(bluetoothDeviceReader);

        DeviceErrorMapper errorMapper = new DeviceErrorMapper();

        return new DeviceViewModel(
                checkNfcAvailabilityUseCase,
                readNfcTagUseCase,
                errorMapper
        );
    }
}

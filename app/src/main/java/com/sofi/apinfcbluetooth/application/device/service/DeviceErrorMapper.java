package com.sofi.apinfcbluetooth.application.device.service;

import com.sofi.apinfcbluetooth.domain.device.result.AppError;

public class DeviceErrorMapper {

    public String map(AppError error) {
        switch (error) {
            case NFC_NOT_SUPPORTED:
                return "El dispositivo no soporta NFC";
            case NFC_DISABLED:
                return "El NFC está desactivado";
            case NFC_INVALID_INTENT:
                return "No se ha detectado un tag NFC válido";
            case NFC_EMPTY_PAYLOAD:
                return "El tag NFC no contiene texto legible";
            case BLUETOOTH_NOT_SUPPORTED:
                return "El dispositivo no soporta Bluetooth";
            case BLUETOOTH_DISABLED:
                return "Bluetooth está desactivado";
            default:
                return "Ha ocurrido un error inesperado";
        }
    }
}

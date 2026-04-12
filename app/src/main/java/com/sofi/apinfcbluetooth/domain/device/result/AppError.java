package com.sofi.apinfcbluetooth.domain.device.result;

public enum AppError {
    NFC_NOT_SUPPORTED,
    NFC_DISABLED,
    NFC_INVALID_INTENT,
    NFC_EMPTY_PAYLOAD,

    BLUETOOTH_NOT_SUPPORTED,
    BLUETOOTH_DISABLED,

    UNKNOWN
}
package com.sofi.apinfcbluetooth.domain.device.port;

import android.content.Intent;

import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

public interface NfcTagReader {
    AppResult<NfcTagData> read(Intent intent);
}

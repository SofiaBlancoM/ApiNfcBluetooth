package com.sofi.apinfcbluetooth.application.device.usecase;

import android.content.Intent;

import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.port.NfcTagReader;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

public class ReadNfcTagUseCase {

    private final NfcTagReader reader;

    public ReadNfcTagUseCase(NfcTagReader reader) {
        this.reader = reader;
    }

    public AppResult<NfcTagData> execute(Intent intent) {
        return reader.read(intent);
    }
}
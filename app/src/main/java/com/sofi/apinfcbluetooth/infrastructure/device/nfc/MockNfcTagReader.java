package com.sofi.apinfcbluetooth.infrastructure.device.nfc;

import android.content.Intent;


import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.port.NfcTagReader;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MockNfcTagReader implements NfcTagReader {

    @Override
    public AppResult<NfcTagData> read(Intent intent) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        NfcTagData data = new NfcTagData(
                "04A224B91F6580",
                "NDEF / NFC-A",
                "Empleado=Sofía Blanco; Centro=Cartagena; Acceso=Permitido",
                now,
                true,
                128,
                "READ_SUCCESS",
                "Lectura simulada desde entorno de desarrollo"
        );

        return new AppResult.Success<>(data);
    }
}
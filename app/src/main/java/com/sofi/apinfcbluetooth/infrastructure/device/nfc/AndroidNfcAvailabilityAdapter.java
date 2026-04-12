package com.sofi.apinfcbluetooth.infrastructure.device.nfc;

import android.content.Context;
import android.nfc.NfcAdapter;

import com.sofi.apinfcbluetooth.domain.device.port.NfcAvailabilityPort;

public class AndroidNfcAvailabilityAdapter implements NfcAvailabilityPort {

    private final Context context;

    public AndroidNfcAvailabilityAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean isSupported() {
        return NfcAdapter.getDefaultAdapter(context) != null;
    }

    @Override
    public boolean isEnabled() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        return adapter != null && adapter.isEnabled();
    }
}

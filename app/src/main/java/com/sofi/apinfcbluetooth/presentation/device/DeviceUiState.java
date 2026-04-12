package com.sofi.apinfcbluetooth.presentation.device;

import com.sofi.apinfcbluetooth.domain.device.model.BluetoothDevice;
import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;

import java.util.ArrayList;
import java.util.List;
public class DeviceUiState {

    private boolean loading;
    private boolean nfcReadingEnabled;
    private String errorMessage;
    private NfcTagData nfcTagData;

    public DeviceUiState() {
        this.loading = false;
        this.nfcReadingEnabled = false;
        this.errorMessage = null;
        this.nfcTagData = null;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isNfcReadingEnabled() {
        return nfcReadingEnabled;
    }

    public void setNfcReadingEnabled(boolean nfcReadingEnabled) {
        this.nfcReadingEnabled = nfcReadingEnabled;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public NfcTagData getNfcTagData() {
        return nfcTagData;
    }

    public void setNfcTagData(NfcTagData nfcTagData) {
        this.nfcTagData = nfcTagData;
    }
}

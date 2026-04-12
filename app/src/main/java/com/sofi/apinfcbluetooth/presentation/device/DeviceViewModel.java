package com.sofi.apinfcbluetooth.presentation.device;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.sofi.apinfcbluetooth.application.device.service.DeviceErrorMapper;
import com.sofi.apinfcbluetooth.application.device.usecase.CheckNfcAvailabilityUseCase;
import com.sofi.apinfcbluetooth.application.device.usecase.ReadNfcTagUseCase;
import com.sofi.apinfcbluetooth.domain.device.model.DeviceAvailability;
import com.sofi.apinfcbluetooth.domain.device.model.NfcTagData;
import com.sofi.apinfcbluetooth.domain.device.result.AppResult;

public class DeviceViewModel extends ViewModel {

    private final CheckNfcAvailabilityUseCase checkNfcAvailabilityUseCase;
    private final ReadNfcTagUseCase readNfcTagUseCase;
    private final DeviceErrorMapper errorMapper;

    private final DeviceUiState uiState = new DeviceUiState();

    public DeviceViewModel(
            CheckNfcAvailabilityUseCase checkNfcAvailabilityUseCase,
            ReadNfcTagUseCase readNfcTagUseCase,
            DeviceErrorMapper errorMapper
    ) {
        this.checkNfcAvailabilityUseCase = checkNfcAvailabilityUseCase;
        this.readNfcTagUseCase = readNfcTagUseCase;
        this.errorMapper = errorMapper;
    }

    public DeviceUiState getUiState() {
        return uiState;
    }

    public void enableNfcReader() {
        DeviceAvailability availability = checkNfcAvailabilityUseCase.execute();

        switch (availability) {
            case READY:
                uiState.setErrorMessage(null);
                uiState.setNfcReadingEnabled(true);
                break;
            case NOT_SUPPORTED:
                uiState.setNfcReadingEnabled(false);
                uiState.setErrorMessage("El dispositivo no soporta NFC");
                break;
            case DISABLED:
                uiState.setNfcReadingEnabled(false);
                uiState.setErrorMessage("El NFC está desactivado");
                break;
        }
    }

    public void simulateNfcRead(Runnable onStateChanged) {
        uiState.setLoading(true);
        uiState.setErrorMessage(null);
        onStateChanged.run();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AppResult<NfcTagData> result = readNfcTagUseCase.execute(new Intent());

            uiState.setLoading(false);

            if (result instanceof AppResult.Success) {
                NfcTagData data = ((AppResult.Success<NfcTagData>) result).getData();
                uiState.setNfcTagData(data);
                uiState.setErrorMessage(null);
            } else {
                AppResult.Failure<NfcTagData> failure = (AppResult.Failure<NfcTagData>) result;
                uiState.setErrorMessage(errorMapper.map(failure.getError()));
            }

            onStateChanged.run();
        }, 1800);
    }
}